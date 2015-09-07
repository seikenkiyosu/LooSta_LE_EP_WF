import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

import Agent.Agent_EP;
import Interaction.Interaction_EP;


class Loosta_LE_EP_WF{
	
	public static final long Roundnum = 1000000000000L;
	public static final int DataNum = 100;	//データ数
	
	public static final int s_from = 11;			//sはs_from=s_toのデータをとる
	public static final int s_to = 12;
	public static final int sinterval = 1;
	
	public static final int ninterval = 10;
	public static final int n_from = 10;	//nはn_from～n_toのデータをとる
	public static final int n_to = 50;	

	
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
        
        for(int s=s_from; s<=s_to; s+=sinterval){
	        for(int n=n_from; n<=n_to; n+=ninterval){
	    		Agent_EP agent[] = new Agent_EP[n];
				long CTsum=0, HTsum=0;
				double CTave=0.0 , HTave=0.0;
	    		
				for(int Data=0; Data < DataNum; Data++){
					long CT = 0, HT = 0;
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
						
						if(IsSafeConfiguration(s, n, leadercount, agent)==true){ 
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
	//					for(int j=0; j<n; j++) agent[j].Countdown();	//timer���J�E���g����
			
						if(CT_count_flag==true) CT++;
					}
				    CTsum += CT;
				    if(CT+HT == Roundnum) { HT = Roundnum; }
				    HTsum += HT;
					System.out.println("(s:" + s +  ", n:" + n_from + "~" + n_to + " )"
							+ "\t n = " + n
							+ ", Data number = " + (Data+1)
							+ ",\tCT = " + CT + ",\tHT = " + HT);
				}	//Dataのfor文終了
				CTave = (double)CTsum / DataNum;
				HTave = (double)HTsum / DataNum;
				/*ファイル書き込みのための処理*/
				try{
			        String stringCTave = String.valueOf(CTave);
			        String stringHTave;
			        if(HTave != 0){
			        	stringHTave = String.valueOf(HTave);
			        }
			        else { stringHTave = "Not Have a safe configuration";}
			        
					String nvalue = new Integer(n).toString();
					String sfromvalue = new Integer(s_from).toString();
					String stovalue = new Integer(s_to).toString();
	
			        File file_nCT = new File(WritingPath + "CT\\" + "n=" + nvalue + "_s=from" + sfromvalue + "to" + stovalue + "_EP" + ".txt");
			        File file_nHT = new File(WritingPath + "HT\\" + "n=" + nvalue + "_s=from" + sfromvalue + "to" + stovalue + "_EP" + ".txt");
	
			        if(!file_nCT.exists()){ file_nCT.createNewFile(); }
			        if(!file_nHT.exists()){ file_nHT.createNewFile(); }
	
			        PrintWriter pw_nCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_nCT, true)));
			        PrintWriter pw_nHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_nHT, true)));
	
			        pw_nCT.write(stringCTave + "\r\n");
			        pw_nHT.write(stringHTave + "\r\n");
	
			        pw_nCT.close();
			        pw_nHT.close();
			        
					String svalue = new Integer(s).toString();
					String nfromvalue = new Integer(n_from).toString();
					String ntovalue = new Integer(n_to).toString();
	
			        File file_sCT = new File(WritingPath + "CT\\" + "s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + "_EP" + ".txt");
			        File file_sHT = new File(WritingPath + "HT\\" + "s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + "_EP" + ".txt");
	
			        if(!file_sCT.exists()){ file_sCT.createNewFile(); }
			        if(!file_sHT.exists()){ file_sHT.createNewFile(); }
	
			        PrintWriter pw_sCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_sCT, true)));
			        PrintWriter pw_sHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file_sHT, true)));
	
			        pw_sCT.write(stringCTave + "\r\n");
			        pw_sHT.write(stringHTave + "\r\n");
	
			        pw_sCT.close();
			        pw_sHT.close();
	
			      }catch(IOException e){
			        System.out.println(e);
			      }
			}	//end  for(int n=n_from; n<=n_to; n++)
		}	//end for(int s=s_from; s<=s_to; s++)
	}
	/*安全状態であるかを返す*/
	private static boolean IsSafeConfiguration(int timerupper, int agentnum, int leadercount, Agent_EP agent[]){
		boolean issafe = true;
		if(leadercount==1){
			for(int i=0; i < agentnum; i++){
				if(agent[i].gettimer() < timerupper/2){
					issafe = false;
					break;
				}
			}
			if(issafe == true){ return true; }	//leaderが一個かつ全個体のタイマがs/2以上のとき安全状態
		}
		return false;
	}
}
