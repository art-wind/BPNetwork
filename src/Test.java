import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Scanner;


public class Test {
    //Test for github
	public static void main(String[]args) throws IOException{
		//Read in the weight matrix and super arguments
		//从文件中读取所需要的BP神经网络
		//读取路径
        int freeone=0;
		BPNetwork network = readBPNetwork("./data/Network.obj");

		String inputOrder = "Type 'f' to input a file\n"+"or Type 't' to read in termianl\n"
				+"Type 'q' to quit";

		BufferedReader strin=new BufferedReader(new InputStreamReader(System.in)); 

		System.out.println(inputOrder);

		//系统读入命令
		String command = strin.readLine();  
		int numI = network.getInputNum();
		while(!command.equals("q"))
		{	
			//Input a file
			if(command.equals("f")){
				
				//File testFile = new File("./data/lab1_test_case.txt");
				File testFile = new File("./data/lab1_p_3.txt");
				Scanner fileInput = null;
				try {
					fileInput = new Scanner(testFile);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int inputNum = network.getInputNum();
				int outputNum = network.getOutputNum();
				double[] input = new double[inputNum];
				double[] output = new double[outputNum];

				//比较测试集合中对错的个数
				int truePre=0;


				while(fileInput.hasNext())
				{
					for (int i =0 ;i<inputNum;i++)
					{
						input[i]=fileInput.nextDouble();
					}
					for (int i =0 ;i<outputNum;i++){
						output[i]=fileInput.nextDouble();
					}
					if(network.test(input,output))
					{
						truePre++;
					}

				}
				fileInput.close();
				System.out.println("True times: "+truePre);
				System.out.println(inputOrder);
				command = strin.readLine();

			}
			else {
				//从控制台读入一行数据
				if(command.equals("t"))
				{

					System.out.println("Please type in the full line of data");
					System.out.println("e.g: \"1 2 3 4 5 6\"");

					//Input the 
					String dataStream = strin.readLine();
					double[]inputArray= new double[numI];
					Scanner scanner = new Scanner(dataStream);
					boolean isValid=true;
					for(int i=0;i<numI;i++)
					{
						if(scanner.hasNext())
							inputArray[i] = scanner.nextDouble();
						else{
							System.out.println("Not valid input!!! Input Size is less than required.!");
							isValid = false;
							break;
						}
					}
					if(isValid)
					{
						network.init(inputArray,true);
						network.printOut();
					}


				}
				else{
					System.out.println("Invalid input!!!");
					System.out.println("Type 'f' to input a file");
					System.out.println("or Type 't' to read in termianl");

				}
				System.out.println(inputOrder);
				command = strin.readLine(); 
			}

		}
	}
	private static BPNetwork readBPNetwork(String path) throws IOException
	{
		FileInputStream fin=new FileInputStream(path);
		ObjectInputStream in=new ObjectInputStream(fin);
		try {
			return (BPNetwork) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fin.close();
		in.close();
		return null;

	}


}
