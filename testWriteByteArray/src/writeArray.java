//import java.io.ByteArrayOutputStream;  
//import java.io.File;  
//import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.util.Arrays;


public class writeArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 10;
		byte buffer[] = new byte[n];
		 writeArray writer=new writeArray();
		 writer.createOutFile("home.txt");
		while((n--)>0)
		{
			int num=65+n;
			Arrays.fill(buffer, (byte)num);
			writer.writeOutFile(buffer);
		}
		writer.closeOutFile();
	}
	/*
    //��byte����д���ļ�  
public void createFile(String path, byte[] content) throws IOException {  

    FileOutputStream fos = new FileOutputStream(path);  

    fos.write(content);  
    fos.close();  
}  */
      public  FileOutputStream fout;
      public void createOutFile(String path)
      {
    	 try{ 
    	  fout=new FileOutputStream(path); //create
    	 }catch (IOException ioe) {
 			ioe.printStackTrace();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
      }
      /*
      try {
			 
			int n = 50;
			byte buffer[] = new byte[n];
			Arrays.fill(buffer, (byte)65);
			
			// ��ȡ��׼������

			// �����ļ����������
			FileOutputStream os = new FileOutputStream(
					"C:\\Users\\sdt03934.DIGITAL\\Desktop\\1.txt");
			// д�������
			os.write(buffer, 0, buffer.length);
			// �ر������
			os.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
      public void  writeOutFile(byte buffer[]){
    	  try{
    		// д�������
  			fout.write(buffer);
    	    	  }catch (IOException ioe) {
    	  			ioe.printStackTrace();
    	  		} catch (Exception e) {
    	  			e.printStackTrace();
    	  		}
    	  
      }
      public void closeOutFile()//close
      {
    	  try{
    	fout.close();  
    	  }catch (IOException ioe) {
  			ioe.printStackTrace();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
      }

}
