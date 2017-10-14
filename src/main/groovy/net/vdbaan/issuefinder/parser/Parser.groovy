/*
 *  Copyright (C) 2017  S. van der Baan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.vdbaan.issuefinder.parser

import groovy.json.JsonSlurper
import groovy.util.logging.Log
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

import javax.xml.parsers.SAXParserFactory

@Log
abstract class Parser {

    def content

    static XmlParserPool parserPool = new XmlParserPool(1000)
    static XmlSlurper xmlslurper
    static JsonSlurper jsonSlurper

    static {
        xmlslurper = new XmlSlurper(false, false, false)
        xmlslurper.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        xmlslurper.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        xmlslurper.setErrorHandler(null)
        jsonSlurper = new JsonSlurper()
    }

    static Parser getParser(file) {
        preload = (Map) Config.getInstance().getProperty(Config.PRELOAD_FILTER)
        try {
            def parser = parserPool.borrowObject()
            def content = new XmlSlurper(parser).parseText(file)

            parserPool.returnObject(parser)
            if (NessusParser.identify(content)) return new NessusParser(content)
            if (NMapParser.identify(content)) return new NMapParser(content)
            if (NetsparkerParser.identify(content)) return new NetsparkerParser(content)
            if (NiktoParser.identify(content)) return new NiktoParser(content)
            if (ArachniParser.identify(content)) return new ArachniParser(content)
            if (BurpParser.identify(content)) return new BurpParser(content)
            if (SSLyzeParser.identify(content)) return new SSLyzeParser(content)
            if (ZAPParser.identify(content)) return new ZAPParser(content)

        } catch (Exception e) {
            log.warning e.getMessage()
            try {
                def json = jsonSlurper.parseText(file)
                if (TestSSLParser.identify(json)) return new TestSSLParser(json)
            } catch (Exception e2) {
                log.warning e2.getMessage()
                return null
            }
        }

//        throw RuntimeException("Parser not found for file: "+file)
    }

    abstract List<Finding> parse()

    static boolean identify(content) { throw RuntimeException('Need to implement this') }

    static Map preload = (Map) Config.getInstance().getProperty(Config.PRELOAD_FILTER)

    boolean allowed(Finding.Severity severity) {
        return preload.get(severity)
    }
}

class XmlParserPoolableObjectFactory extends BasePooledObjectFactory {
    private SAXParserFactory parserFactory

    XmlParserPoolableObjectFactory() {
        parserFactory = SAXParserFactory.newInstance()
        parserFactory.setValidating(false)
        parserFactory.setNamespaceAware(false)
        parserFactory.setXIncludeAware(false)
        parserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    }

    Object create() throws Exception {
        return parserFactory.newSAXParser()
    }

    @Override
    PooledObject wrap(Object obj) {
        return new DefaultPooledObject(obj)
    }
// Other methods left empty
}

class XmlParserPool {
    private final GenericObjectPool pool

    XmlParserPool(int maxActive) {
        def gen = new GenericObjectPoolConfig()
        gen.setBlockWhenExhausted(true)
        gen.setMaxTotal(maxActive)
        gen.setMaxWaitMillis(0)
        pool = new GenericObjectPool(new XmlParserPoolableObjectFactory(), gen)
    }

    Object borrowObject() throws Exception {
        return pool.borrowObject()
    }

    void returnObject(Object obj) throws Exception {
        pool.returnObject(obj)
    }
}