import java.io.Serializable;


public class BPNetwork implements Serializable{
	private int inputNum,hiddenNum,outputNum;
	private double rate;
	private double[]input;
	private double[]hidden;
	private double[]output;
	
	//存储两个权重矩阵
	private double[][]wgtIH;
	private double[][]wgtHO;
	BPNetwork()
	{

	}
	
	//构造函数 通过给定的设定参数初始化实例
	BPNetwork(int inNum,int hiNum,int outNum,double r)
	{
		inputNum  = inNum;
		hiddenNum = hiNum;
		outputNum = outNum;
		rate=r;

		input = new double[inputNum+1];
		input[inputNum] =1;//Bias for input

		hidden = new double[hiddenNum+1];
		hidden[hiddenNum]=1;//Bias for hidden

		output = new double[outputNum];

		wgtIH = new double[inputNum+1][hiddenNum];
		wgtHO = new double[hiddenNum+1][outputNum];
		for(int i=0;i<inputNum+1;i++)
		{
			for(int j=0;j<hiddenNum;j++)
			{
				wgtIH[i][j]=(2*Math.random()-1);
			}
		}
		for(int i=0;i<hiddenNum+1;i++)
		{
			for(int j=0;j<outputNum;j++)
			{
				wgtHO[i][j]=(2*Math.random()-1);
			}
		}
	}

	//构造函数 通过给定的
	BPNetwork(int inNum,int hiNum,int outNum,double r,double[][]arrayIH,double[][] arrayHO)
	{
		inputNum  = inNum;
		hiddenNum = hiNum;
		outputNum = outNum;

		input = new double[inputNum+1];
		input[inputNum] =1;//Bias for input

		hidden = new double[hiddenNum+1];
		hidden[hiddenNum]=1;//Bias for hidden

		output = new double[outputNum];
		wgtIH = new double[inputNum+1][hiddenNum];
		wgtIH = arrayIH;
		wgtHO = new double[hiddenNum+1][outputNum];
		wgtHO = arrayHO;
	}

	
	// 依照输入的数组根据权重进行初始化
	// 再比较输出数组和理想数组中的最大数的下标来确定结果的正确与否
	boolean test(double[]inputArray,double[]outputArray)
	{
		//System.out.println("Test:");
		//False代表output数组不需要加sigmo函数
		init(inputArray,true);

		//调用biggest方法调出数组的最大值的下标志
		int predictBig=biggest(output);
		int expBig=biggest(outputArray);

		//进行比较 
		//如果相同,打印并返回正确值
		if(predictBig==expBig)
		{
			System.out.println("Success! "+predictBig);
			return true;
		}
		else
		{
			System.out.print("失败！！！");
			System.out.println("预测值为 "+output[expBig]);
			return false;
		}
		//System.out.print("预测结果为: "+predictBig);

		//System.out.println("           实际结果为: "+expBig);

		//System.out.println("预测数据: "+predictBig+" "+output[predictBig]);

	}

	//返回数组中最大数的下标
	int biggest(double[]arr){
		int index=0;
		double big=0;
		for(int i=0;i<arr.length;i++)
		{
			if(big<arr[i])
			{
				big=arr[i];
				index=i;
			}
		}
		return index;
	}
	void init(double[]inputArray,boolean isLearning){

		//初始化输入数组
		for(int i=0;i<inputArray.length;i++){
			input[i]=inputArray[i];
			//System.out.print("print:: "+input[i]+"\n");
		}

		//依据I->H的权重矩阵初始化中间层数值
		for (int j=0;j<hiddenNum;j++)
		{
			hidden[j]=0;
			for(int i=0;i<inputNum+1;i++)
			{
				hidden[j]+=(input[i]*wgtIH[i][j]);
			}

			//sigmoid化将值域放在0-1之内
			hidden[j] = sigmo(hidden[j]);
		}

		//初始化输出数组
		for(int k=0;k<outputNum;k++)
		{
			output[k]=0;
			for(int j=0;j<hiddenNum+1;j++)
			{
				output[k]+=(hidden[j]*wgtHO[j][k]);
			}
			if(isLearning)
				output[k] = sigmo(output[k]);
			//System.out.println("out: "+output[k]);
		}
	}
	//调整权重
	double learn(double[]testInput,double[]expOutput){
		double exp=0;
		double factor=0;
		init(testInput,true);
		double error=0;

		//Compute the error
		for(int i=0;i<outputNum;i++)
		{
			error+=(0.5*Math.pow((output[i]-expOutput[i]),2));
		}
		//调整HIDDEN层到OUTPUT层的权重矩阵
		for(int j=0;j<hiddenNum+1;j++)
		{
			for(int i=0;i<outputNum;i++)
			{
				exp= expOutput[i];
				double delWji = rate*(exp-output[i])*output[i]*(1-output[i])*hidden[j];
				wgtHO[j][i]+=delWji;
			}
		}
		//调整INPUT层到HIDDEN层的权重矩阵
		for(int k=0;k<inputNum+1;k++)
		{
			for(int j=0;j<hiddenNum;j++)
			{
				factor=0;
				for(int i=0;i<outputNum;i++)
				{
					//
					exp= expOutput[i];
					factor += (wgtHO[j][i]*output[i]*(1-output[i]) *(exp-output[i]));					
				}
				//System.out.print("factor:: "+factor);
				double delWkj = rate*hidden[j]*(1-hidden[j])*input[k]*factor;

				wgtIH[k][j]+=delWkj;

			}
		}
		return error;		
	}

	void printHid(){
		System.out.println("Hidden");
		for(double db:hidden)
		{
			System.out.print(db+" ");
		}
		System.out.println();
	}

	void printOut(){
		System.out.println("Out");
		for(int c=0;c<output.length;c++)
		{
			System.out.println("["+c+"] = "+output[c]+" ");
		}
		System.out.println("The answer is "+biggest(output)+".");
	}
	
	void printIH()
	{
		for(int m=0;m<inputNum+1;m++)
		{
			for(int n=0;n<hiddenNum;n++)
			{
				System.out.print(wgtIH[m][n]+" ");
			}
			System.out.println();
		}
	}

	void printHO()
	{
		for(int m=0;m<hiddenNum;m++)
		{
			for(int n=0;n<outputNum;n++)
			{
				System.out.print(wgtHO[m][n]+" ");
			}
			System.out.println();
		}
	}
	double sigmo(double sig){
		//double ret = 1+Math.exp(-sig);
		return 1/(1+Math.exp(-sig));
	}
	public int getInputNum() {
		return inputNum;
	}
	public int getHiddenNum() {
		return hiddenNum;
	}
	public int getOutputNum() {
		return outputNum;
	}
	public double getRate() {
		return rate;
	}
	public String getAttrs(){
		String str="";
		str += (inputNum+" "+hiddenNum+" "+outputNum+" "+rate);
		str += "\n";
		for(int m=0;m<inputNum+1;m++)
		{
			for(int n=0;n<hiddenNum;n++)
			{
				str+=(wgtIH[m][n]+" ");
			}
			str += "\n";
		}
		//str += "\n";
		for(int m=0;m<hiddenNum+1;m++)
		{
			for(int n=0;n<outputNum;n++)
			{
				str+=(wgtHO[m][n]+" ");
			}
			str += "\n";
		}


		return str;

	}


}
