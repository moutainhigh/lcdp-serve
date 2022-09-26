package com.redxun.db;

import org.owasp.esapi.codecs.Codec;

/**
 * SQL Server 编码
 */
public class SqlServerCodec extends Codec {

	@Override
	public String encodeCharacter(char[] immune, Character c) {
		
		char ch = c.charValue();
		if ( ch == 0x00 ) {
			return "\\0";
		}
		if ( ch == 0x08 ){
			return "\\b";
		}
		if ( ch == 0x09 ){
			return "\\t";
		}
		if ( ch == 0x0a ){
			return "\\n";
		}
		if ( ch == 0x0d ) {
			return "\\r";
		}
		if ( ch == 0x1a ) {
			return "\\Z";
		}
		if ( ch == 0x22 ) {
			return "\\\"";
		}
		if ( ch == 0x25 ){
			return "\\%";
		}
		if ( ch == 0x27 ){
			return "\\'";
		}
		if ( ch == 0x5c ){
			return "\\\\";
		}

		
        return ""+c;
	}
	
	

}
