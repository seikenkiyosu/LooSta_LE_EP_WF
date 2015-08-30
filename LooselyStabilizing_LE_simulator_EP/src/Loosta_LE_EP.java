import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import Agent.Agent_EP;
import Interaction.Interaction_EP;

class Loosta_LE_EP{
	public static final int Roundnum = 10000000;
	public static final int DataNum = 200;	//データ数
	
	public static final int s = 192;			//96�ȏ��3n�ȏ�
	public static final int n_from = 30;	//nはn_from～n_toのデータをとる
	public static final int n_to = 64;	
	
	public static String RandomMethod = "Torus";	//Torus or RWP(Random Way Point)
	public static String CoodinateSystem = "Rectanglar";	//直交座標(Rectanglar) or 極座標(Polar)
	public static String name = "星顕";
	//System.getProperty("user.name");
	
	public static String DataPath = "\\Users\\" + name + "\\Dropbox\\Data\\";
	public static String WritingPath = DataPath + "Data_EP\\";

	public static void main(String args[]){
		Random random = new Random();
		
		File file = new File(WritingPath);
        if (!file.exists()) {
            System.out.println("ディレクトリが存在しません。");
            System.exit(-1);
        }
        
        for(int n=n_from; n<=n_to; n++){
    		Agent_EP agent[] = new Agent_EP[n];
			int CTsum=0, HTsum=0;
			double CTave=0.0 , HTave=0.0;
    		
			for(int Data=0; Data < DataNum; Data++){
				int CT = 0, HT = 0;
				boolean HT_count_flag = false, CT_count_flag = true;
				
				//Agentの初期化
				for(int i=0; i<n; i++)
				agent[i] = new Agent_EP(random.nextBoolean(), s);
				
				for(int i=0; i<Roundnum; i++){
					int leadercount=0;
		//			int leaderid=0;
					//リーダの数をかぞえる
					for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leadercount++; }
					
					//Holding Timeが終了したらぬける
					if(leadercount!=1 && HT_count_flag==true){ break; }
					
					if(leadercount==1){ 
								HT_count_flag = true;
								CT_count_flag = false;
							}
					
					if(HT_count_flag==true) HT++;
		
//					System.out.println("the number of leaders = " + leadercount);
					int p=0, q=0;
					while(p==q){					//�𗬂�����̂�I��
						p = random.nextInt(n);		//�����_����agent���Ƃ��Ă���
						q = random.nextInt(n);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
					}
					
					Interaction_EP.interaction(agent[p], agent[q], s);	//�𗬂�����
					for(int j=0; j<n; j++) agent[j].Countdown();	//timer���J�E���g����
		
					if(CT_count_flag==true) CT++;
				}
			    CTsum += CT;
			    HTsum += HT;
				System.out.println("(s:" + s +  ", n:" + n_from + "~" + n_to + " )"
						+ "\t n = " + n
						+ ", Data number = " + (Data+1)
						+ ",\tCT = " + CT + ",\tHT = " + HT);
			}
			CTave = (double)CTsum / DataNum;
			HTave = (double)HTsum / DataNum;
			/*ファイル書き込みのための処理*/
			try{
				String svalue = new Integer(s).toString();
				String nfromvalue = new Integer(n_from).toString();
				String ntovalue = new Integer(n_to).toString();

		        File fileCT = new File(WritingPath + "CT" + "_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + "_EP" + ".txt");
		        File fileHT = new File(WritingPath + "HT" + "_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + "_EP" + ".txt");

		        if(!fileCT.exists()){ fileCT.createNewFile(); }
		        if(!fileHT.exists()){ fileHT.createNewFile(); }

		        PrintWriter pwCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileCT, true)));
		        PrintWriter pwHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileHT, true)));

		        String stringCTave = String.valueOf(CTave);
		        String stringHTave = String.valueOf(HTave);

		        pwCT.write(stringCTave + "\r\n");
		        pwHT.write(stringHTave + "\r\n");

		        pwCT.close();
		        pwHT.close();

		      }catch(IOException e){
		        System.out.println(e);
		      }
		}
	}
}
