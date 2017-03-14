package com.ssj.jdbcfront.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

@SuppressWarnings("deprecation")
public class ResourceLoader extends FileResourceLoader {

	@Override
	public InputStream getResourceStream(String arg0) throws ResourceNotFoundException {

		if(!super.resourceExists(arg0)){
			if(StringUtil.isNotBlank(Engine.charSet)){
				try {
					return new ByteArrayInputStream(arg0.getBytes(Engine.charSet));
				} catch (UnsupportedEncodingException e) {
					LogUtil.logError(e);
				}
			}
			return new StringBufferInputStream(arg0);
		}
		return super.getResourceStream(arg0);
	}

	@Override
	public boolean resourceExists(String arg0) {
		return true;
	}

}
