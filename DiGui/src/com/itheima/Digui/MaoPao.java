package com.itheima.Digui;

public class MaoPao {
	public static void main(String[] args) {
		int[] a = {7,1,2,4,5,3,6};
		int count = 0;
		int count1 = 0;
		for(int i=0;i<a.length;i++){
			boolean b = true;
			for(int j=i+1;j<a.length;j++){
				if(a[i]>a[j]){
					b = false;
					int temp = a[j];
					a[j] = a[i];
					a[i] = temp;
				}
				count++;
			}
			count1++;
			if(b){
				break;
			}
		}
		for(int k=0;k<a.length;k++){
			System.out.print(a[k] + " ");
		}
		System.out.println("内循环次数" + count);
		System.out.println("外循环次数" + count1);
}
}
