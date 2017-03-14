package com.ssj.jdbcfront.util;

import com.ssj.util.StringUtil;


public class EqualsBuilder extends org.apache.commons.lang.builder.EqualsBuilder {

    public EqualsBuilder append(String lhs, String rhs) {
    	if (!this.isEquals())
    	    return this;
    	this.setEquals(StringUtil.isEqual(lhs, rhs));
    	return this;
    }

	@Override
	public EqualsBuilder append(boolean arg0, boolean arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(boolean[] arg0, boolean[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(byte arg0, byte arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(byte[] arg0, byte[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(char arg0, char arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(char[] arg0, char[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(double arg0, double arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(double[] arg0, double[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(float arg0, float arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(float[] arg0, float[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(int arg0, int arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(int[] arg0, int[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(long arg0, long arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(long[] arg0, long[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(Object arg0, Object arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(Object[] arg0, Object[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(short arg0, short arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

	@Override
	public EqualsBuilder append(short[] arg0, short[] arg1) {
		
		return (EqualsBuilder)super.append(arg0, arg1);
	}

}
