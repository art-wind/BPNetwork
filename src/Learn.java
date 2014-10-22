import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Scanner;


public class Learn {
	public static void main(String[]atgs) throws IOException{
		
		
		//设定超参数
		int inputNum = 13;
		int hiddenNum = 80;
		int outputNum = 30; 
		double rate=0.3;								
		double[]input=new double[inputNum];
		double[]output =new double[outputNum];
		
		/*
		 * 
		//预留的从控制台读取输入
		int numI = 0,numH=0,numO=0;
		double rate_i=0;
		try {  
            BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));  
            System.out.println("Please input the number of [Input Neuros]");  
            String str1 = strin.readLine();  
            numI = Integer.parseInt(str1);  
            
            System.out.println("Please input the number of [Hidden Neuros]");  
            String str2 = strin.readLine(); 
            numH = Integer.parseInt(str2); 
            
            System.out.println("Please input the number of [Output Neuros]");  
            String str3 = strin.readLine();
            numO = Integer.parseInt(str3); 
            
            System.out.println("Please input the Learning [Rate]");  
            String rate_str = strin.readLine();
            rate_i = Double.parseDouble(rate_str);
            strin.close();
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
        int inputNum = numI;
		int hiddenNum = numH;
		int outputNum = numO; 
		double rate=rate_i;*/
		
		
		BPNetwork bp1 = new BPNetwork(inputNum,hiddenNum,outputNum,rate);
		
		int times =5000;//4_0000/4=1_0000
		//相当于每个样本读1万次
		for(int k=0;k<times;k++)
		{
			int fileCount = k%6+1;
			if(fileCount!=3)
			{
				File file = new File("data/lab1_p_"+fileCount+".txt");
				
				Scanner fileInput = null;
				try {
					fileInput = new Scanner(file);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//统和误差
				double avgError=0;
				int lineNumber=0;
				while(fileInput.hasNext()){
					lineNumber++;
					for (int i =0 ;i<inputNum;i++)
					{
						input[i] = fileInput.nextDouble();
						//System.out.print();
					}
					for (int i =0 ;i<outputNum;i++){
						output[i] = fileInput.nextDouble();
					}
					avgError+=bp1.learn(input,output);
				}
				
				avgError/=lineNumber;
				System.out.println("Times "+k+":  "+avgError);
				fileInput.close();
			}
			
		}
		
		//将神经网络输出到外部
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("./data/Network.obj");
			ObjectOutputStream out=new ObjectOutputStream(fout);
			out.writeObject(bp1);
			out.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
}
