package com.neo.neoandroidlib;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.neo.neoapp.entity.Message;

import cz.msebera.httpclient.android.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	
	private static  String Tag = "FileUtils";
	
	public static String getSysPathPath(){
		return Environment.getRootDirectory().getAbsolutePath()+"/";//system
	}
	
	public static String getDataPath(){
		return Environment.getDataDirectory().getAbsolutePath()+"/";///data
	}
	
	public static String getDldCachePath()
	{
		return Environment.getDownloadCacheDirectory().getAbsolutePath()+"/";//cache
	}
	
	public static String getSDPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath()+"/";//sdcard
	}
	
	public static String getSDPicPath(Context context)
	{
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/";
	}
	
	public static String getAppDataPath(Context context)
	{
		String path = "";
		try{
			path = context.getExternalFilesDir(null).getAbsolutePath()+"/";
		}catch (Exception e) {
			Log.e(Tag, e.toString());
			path = "";
		}
		return path;
	}
	
	public static String getAppCachePath(Context context)
	{
		return context.getExternalCacheDir().getAbsolutePath()+"/";
	}
	
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static boolean createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			return dir.mkdirs();
		}
		return true;
	}

	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}
	
	public static void overrideContent(String fileName, String content) {  
        try {  
        	if (fileName==null||content==null)
        		return;
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
            FileWriter writer = new FileWriter(fileName, false);  
            writer.write(content);  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	
	public static void appendToEnd(String fileName, String content) {  
        try {  
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
            FileWriter writer = new FileWriter(fileName, true);  
            writer.write(content);  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
    public static boolean isFileExist(String path) {
        if (new File(path).exists()) {
            return true;
        }
        return false;
    }

    public static long getFilesize(String path) {
        File file = new File(path);
        if (file.exists()) {
            return 0;
        }
        return file.length();
    }

    public static String getJson(Context context, String name) {
        Throwable th;
        String str = null;
        if (name != null) {
            File file = new File(getAppDataPath(context) + name);
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    FileInputStream fis2 = new FileInputStream(file);
                    try {
                        str = readTextFile(fis2);
                        if (fis2 != null) {
                            try {
                                fis2.close();
                            } catch (IOException e) {
                                fis = fis2;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fis = fis2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    if (fis != null) {
                        try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    return str;
                } catch (Throwable th3) {
                    th = th3;
                    if (fis != null) {
                        try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    try {
						throw th;
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
        return str;
    }

    public static String readTextFile(InputStream inputStream) {
        String readedStr = BuildConfig.VERSION_NAME;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, AsyncHttpResponseHandler.DEFAULT_CHARSET));
            while (true) {
                String tmp = br.readLine();
                if (tmp == null) {
                    br.close();
                    inputStream.close();
                    return readedStr;
                }
                readedStr = new StringBuilder(String.valueOf(readedStr)).append(tmp).toString();
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    public static int moveFile(String oldPath, String newPath) {
        int bytesum = 0;
        try {
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (newfile.exists()) {
                newfile.delete();
            }
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int byteread = inStream.read(buffer);
                    if (byteread == -1) {
                        break;
                    }
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("\u590d\u5236\u5355\u4e2a\u6587\u4ef6\u64cd\u4f5c\u51fa\u9519");
            e.printStackTrace();
        }
        return bytesum;
    }

    public static int copyFile(String oldPath, String newPath) {
        int bytesum = 0;
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int byteread = inStream.read(buffer);
                    if (byteread == -1) {
                        break;
                    }
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("\u590d\u5236\u5355\u4e2a\u6587\u4ef6\u64cd\u4f5c\u51fa\u9519");
            e.printStackTrace();
        }
        return bytesum;
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs();
            String[] file = new File(oldPath).list();
            for (int i = 0; i < file.length; i++) {
                File temp;
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(new StringBuilder(String.valueOf(oldPath)).append(file[i]).toString());
                } else {
                    temp = new File(new StringBuilder(String.valueOf(oldPath)).append(File.separator).append(file[i]).toString());
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(new StringBuilder(String.valueOf(newPath)).append("/").append(temp.getName().toString()).toString());
                    byte[] b = new byte[5120];
                    while (true) {
                        int len = input.read(b);
                        if (len == -1) {
                            break;
                        }
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(new StringBuilder(String.valueOf(oldPath)).append("/").append(file[i]).toString(), new StringBuilder(String.valueOf(newPath)).append("/").append(file[i]).toString());
                }
            }
        } catch (Exception e) {
            System.out.println("\u590d\u5236\u6574\u4e2a\u6587\u4ef6\u5939\u5185\u5bb9\u64cd\u4f5c\u51fa\u9519");
            e.printStackTrace();
        }
    }

    public static boolean isDirEmpty(String path) {
        File file = new File(path);
        if (!file.isDirectory() || file.list().length > 0) {
            return false;
        }
        return true;
    }
    
    public static <T> boolean writeObjectToFile(String path, T object){
    	FileOutputStream fos = null;
    	ObjectOutputStream oos = null;
    	try {
			fos = new FileOutputStream(path);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T readObjectFromFile(String path,T object){
    	FileInputStream fis = null;
    	ObjectInputStream ois = null;
		
    	try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			object = (T) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return object;
    	
    }
    
    @SuppressWarnings("unchecked")
	public static List<Message> readMessageListFromFile(String path){
    	FileInputStream fis = null;
    	ObjectInputStream ois = null;
    	List<Message> msgList = null;
    	try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			msgList =  (List<Message>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return msgList;
    	
    }
    
    private static  void test(){
    	List<String> list = new ArrayList<String>();
    	writeObjectToFile(".",list);
    	try {
			readObjectFromFile(".",Class.forName("List<String>"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
