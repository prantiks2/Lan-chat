/*
lanchat-version:2\n
server function:-\n
type-1-Global chat mode.\n
clients connect to global chat mode and enter their name and chat with other members connected to the global server.\n
type-2-Private chat mode.\n
server is created on a specified port and members connect to the server on that port.\nThe number of members are specified during the creation of server.\n
server commands:-\n
addclient-to add new member to a private chat when more members are to join than what was specified.\n
kick <user name>-to forcefully make a user exit the conversation.\n
shutdown-to close the server.\n
client commands:-\n
close-to leave the conversation.
*/


import java.io.*;
import java.net.*;
import java.util.*;
import javazoom.jl.player.*;




class sound implements Runnable
{
	static String str=""; 
	sound(String s)
	{
		str=s;
	}
	public void run()
	{
		try
		{
			FileInputStream fis = new FileInputStream(str);
			Player playMP3 = new Player(fis);
			playMP3.play();
		}
		catch(Exception e){}
	}
}












class client implements Runnable
{
	static Socket s;static int flag=0;
	public void run()
	{
		try
		{
			BufferedReader brr=new BufferedReader(new InputStreamReader(s.getInputStream()));
			while(true)
			{
				String m=brr.readLine();
				System.out.println(m);
				if(m.equals("Server-->Server shutdown initiated,Connection to server lost!")||m.equals("Server-->You have been kicked from the conversation!"))
				{
					play("alert.mp3");
					System.out.print("terminal will close in 5 secs!");
					for(int i=0;i<5;i++)
					{
						System.out.print("\b\b\b\b\b\b\b"+(5-i)+" secs!");
						Thread.sleep(1000);
					}
					System.out.print("\b\b\b\b\b\b\b"+"0 secs!");
					System.exit(0);
				}
				sound so=new sound("notification.mp3");
				Thread t=new Thread(so);
				t.start();
			}
		}
		catch(Exception e){}
	}

	void play(String str)throws Exception
	{
		FileInputStream fis = new FileInputStream(str);
		Player playMP3 = new Player(fis);
		playMP3.play();
	}

	public static void main(String[] args)throws Exception
	{
		System.out.println("lanchat-version:2\n...Client Application...\n\nTypes of mode:-\ntype-1-Global chat mode:\nclients connect to global chat mode and enter their name and chat with other members connected to the global server.\ntype-2-Private chat mode:\nserver is created on a specified port and members connect to the server on that port.\nThe number of members are specified during the creation of server.\n\nclient commands:-\nclose-to leave the conversation.\n\n");
		client cl=new client();
		cl.strtcl();
	}

	void strtcl()throws Exception
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String sr="";int f=0,port=0;
		do
		{
			f=0;
			try
			{
				System.out.print("Enter Server Address:");
				sr=br.readLine();
				System.out.print("Enter Server port,Enter 1 to join global chat:");
				port=Integer.parseInt(br.readLine());
				if(port==1)
					port=12370;
				s=new Socket(sr,port);
			}
			catch(Exception e)
			{
				System.out.println("Server not found! Try again....");
				f=1;
			}
		}while(f==1);
		BufferedReader brr=new BufferedReader(new InputStreamReader(s.getInputStream()));
		port=Integer.parseInt(brr.readLine());String n="",msg="";
		System.out.print("Probing");
		for(int i=0;i<3;i++)
		{
			for(long j=0;j<699999999;j++);
			System.out.print(".");
		}
		s.close();
		s=new Socket(sr,port);
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println("Connected");
		System.out.print("Enter your name:");
		n=br.readLine();
		bw.write(n);
		bw.newLine();
		bw.flush();
		client cl=new client();
		Thread listner=new Thread(cl);
		listner.start();
		while(true)
		{
			if(flag==1)
				break;
			msg=br.readLine();
			if(msg.equals(""))
				continue;
			msg=n+":"+msg;
			bw.write(msg);
			bw.newLine();
			bw.flush();
			if(msg.equals(n+":close"))
			{
				listner.stop();
				break;
			}
		}
		System.out.println("disconnected");
		System.out.print("terminal will close in 5 secs!");
		for(int i=0;i<5;i++)
		{
			System.out.print("\b\b\b\b\b\b\b"+(5-i)+" secs!");
			Thread.sleep(1000);
		}
		System.out.print("\b\b\b\b\b\b\b"+"0 secs!");
		s.close();
	}

}



















class server implements Runnable
{
	static ServerSocket s1;
	int i;static int baseport=12371,serverport=12370,user=10000;
	static boolean serverflag=true;
	static LinkedList l=new LinkedList();
	static LinkedList ur=new LinkedList();
	server(int k)
	{
		i=k;
	}
	
	public void run()
	{
		try
		{
			ServerSocket s3=new ServerSocket(baseport+i);
			Socket sc=s3.accept();
			InetAddress addr = sc.getInetAddress();
			System.out.println("Connected ip:"+addr);
			l.add(sc);
			String m="";boolean loop=true;
			BufferedReader brr=new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String usr=brr.readLine();
			ur.add(usr);
			m=m+usr+" joined the conversation.....";
			do
			{
				if(m.equals(usr+":close"))
				{
					m=usr+" left the conversation";
					loop=false;
				}
				System.out.println(m);
				int len=l.size();
				for(int p=0;p<len;p++)
				{
					Socket send=(Socket)l.get(p);
					if(sc!=send)
					{
						BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(send.getOutputStream()));
						bw.write(m);
						bw.newLine();
						bw.flush();
					}
				}
				if(loop==true)
					m=brr.readLine();
			}while(loop);
			l.remove(sc);
			ur.remove(usr);
			sc.close();
		}
		catch(Exception e){}
	}

	public static void main(String[] args)throws Exception
	{
		System.out.println("lanchat-version:2\n...Server Application...\n\nTypes of mode:-\ntype-1-Global chat mode:\nclients connect to global chat mode and enter their name and chat with other members connected to the global server.\ntype-2-Private chat mode:\nserver is created on a specified port and members connect to the server on that port.\nThe number of members are specified during the creation of server.\n\nserver commands:-\naddclient-to add new member to a private chat when more members are to join than what was specified.\nkick <user name>-to forcefully make a user exit the conversation.\nshutdown-to close the server.\n\n");
		int f=0;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		do
		{
			f=0;
			try
			{
				System.out.print("Enter Serverport:-\npress 1 to start global server or enter a port no. to start private server on that port:");
				serverport=Integer.parseInt(br.readLine());
				if(serverport==1)
					serverport=12370;
				else
				{
					do
					{
						f=0;
						System.out.print("Enter no. of chat participants for private chat:");
						user=Integer.parseInt(br.readLine());
						if(user<2||user>100)
						{
							System.out.println("nice joke! please enter no. of users between 2 and 100, try again....");
							f=1;
						}
					}while(f==1);
					if(serverport<25000||serverport>40000)
					{
						System.out.println("port no. out of range!\nPlease Enter value between 25000-40000, Try again...");
						f=1;
					}
				}
				ServerSocket s9=new ServerSocket(serverport);
				s9.close();
			}
			catch(BindException e)
			{
				System.out.println("Global chat server started or given Port already in use!\n  Try again with a different port no.");
				f=1;
			}
			catch(Exception e)
			{
				System.out.println("Invalid port no. or Invalid no. of users!");
				f=1;
			}
		}while(f==1);
		server srvr=new server(0);
		srvr.strtsrvr();
	}

	void strtsrvr()throws Exception
	{
		i=0;
		baseport=serverport+1;
		s1=new ServerSocket(serverport);
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println("Server address:"+addr);
		new srvrctrl();
		try
		{
			do
			{
				Socket s=s1.accept();
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write(String.valueOf(baseport+i));
				bw.newLine();
				bw.flush();
				server sr=new server(i);
				Thread t=new Thread(sr);
				t.start();
				i++;
				user--;
				if(user<1)
				{
					s1.close();
					serverflag=false;
					while(true)
					{
						if(serverflag)
							break;	
						Thread.sleep(1000);
					}
				}
			}while(serverflag==true);
		}catch(Exception e){}
	}

}




















class srvrctrl extends server implements Runnable
{
	srvrctrl()
	{
		super(0);
		Thread t=new Thread(this);
		t.start();
	}
	
	public void run()
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String str="";
			String m="";int len=0;
			while(true)
			{
				str=br.readLine();
				if(str.equals(""))
					continue;
				if(str.equals("shutdown"))
				{
					m="Server-->Server shutdown initiated,Connection to server lost!";
					System.out.println("Server shutdown initiated...");
					send_to_client(m);
					System.exit(0);
				}
				else if(str.length()>5&&(str.substring(0,4)).equals("kick"))
				{
					int fg=0;
					String u=str.substring(5);String uinl="";
					m="Server-->"+u+" was kicked from the conversation!";
					len=l.size();
					for(int p=0;p<len;p++)
					{
						Socket send=(Socket)l.get(p);
						uinl=(String)ur.get(p);
						if(uinl.equals(u))
						{
							l.remove(send);	
							ur.remove(uinl);
							BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(send.getOutputStream()));
							bw.write("Server-->You have been kicked from the conversation!");
							bw.newLine();
							bw.flush();
							send.close();
							fg=1;
							break;
						}
					}
					if(fg==0)
						System.out.println("Server-->No such user online!");
					else
					{
						System.out.println(m);
						send_to_client(m);			
					}
				}
				else if(str.equals("addclient"))
				{
					if(serverflag==false)
					{
						user=user+1;
						s1=new ServerSocket(serverport);
						serverflag=true;
						System.out.println("Server-->Ready to accept new client....");
					}
					else
					{
						System.out.println("Server-->Channel already opened to accept client!");
					}
				}
				else
				{
					str="Server-->"+str;
					send_to_client(str);
				}
			}
		}catch(Exception e){}
	}

	void send_to_client(String str)
	{
		try
		{
			int len=l.size();
			for(int p=0;p<len;p++)
			{
				Socket send=(Socket)l.get(p);
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(send.getOutputStream()));
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
		}catch(Exception e){}
	}
}