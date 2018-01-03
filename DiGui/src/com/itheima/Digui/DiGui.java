package com.itheima.Digui;

public class DiGui {
	public static void main(String[] args){
		System.out.println(diGui(5));
	}

	public static int diGui(int n){
		if(n==1){
			return n;
		}
		return n*diGui(n-1);
	}
}
