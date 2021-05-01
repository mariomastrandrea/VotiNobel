package it.polito.tdp.nobel.model;

public class TestModel
{
	public static void main(String[] args)
	{
		MyVotiNobelModel model = new MyVotiNobelModel();
		System.out.println("getName(): " + model.getClass().getName());
		System.out.println("getSimpleName(): " + model.getClass().getSimpleName());
		System.out.println("getCanonicalName(): " + model.getClass().getCanonicalName());
		System.out.println("toString(): " + model.getClass().toString());

	}
	
}
