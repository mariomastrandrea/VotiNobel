package it.polito.tdp.nobel.model;

public class DoubleObj
{
	public double value;
	
	private DoubleObj(double d)
	{
		this.value = d;
	}
	
	public static DoubleObj of(double value)
	{
		return new DoubleObj(value);
	}
}
