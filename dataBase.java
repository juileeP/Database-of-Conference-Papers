
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.*;

import javax.swing.text.DateFormatter;


public class dataBase {


	//member function (User functionality of the project)
 public void member(int a, Connection con)

    {
	 Statement stmt = null;
     ResultSet rs = null;
     Scanner in = new Scanner(System.in);

            System.out.println(" ");
            System.out.println(" Please enter the basis on whcih you would like to search" +  "the conference papers ");
            System.out.println("1. Conference Paper category ");
            System.out.println("2. Publishers ");
            System.out.println("3. Conference Name ");
            System.out.println(" ");
            String choice = "";
            choice = in.nextLine();

            //querying based on the category of the paper
            if(choice.equals("1"))
			{
			    String category = "";
                System.out.println(" Enter the Paper category ");
                category = in.nextLine();
                System.out.println(" The category you entered is : "+category);
                System.out.println(" ");
                try {
                   String Query = "select * from Conference_Paper where category like '%"+category+"%'";
                   stmt = con.prepareStatement(Query);
       			   rs = stmt.executeQuery(Query);
       			 display(rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            //querying based on author/publisher names
			else if(choice.equals("2"))
			{
                String pubName = "";
                System.out.println(" Enter the Publisher name ");
                pubName = in.nextLine();
                System.out.println(" The Publisher Name you entered is : "+pubName);
                System.out.println(" ");
                try {
                    String Query = "select * from Conference_Paper where authorName like '%"+pubName+"%'";
                    stmt = con.prepareStatement(Query);
        			   rs = stmt.executeQuery(Query);
        			   display(rs);
                } catch (SQLException e){

                }
            }

            //querying based on conference name
			else if(choice.equals("3"))
			{
                String confName = "";
                System.out.println(" Enter the Conference name ");
                confName = in.nextLine();
                System.out.println(" The Conference Name you entered is : "+confName);
                System.out.println(" ");
                try {
                    String Query = "select * from Conference_Paper where conference_name like '%"+confName+"%'";
                    stmt = con.prepareStatement(Query);
     			   rs = stmt.executeQuery(Query);
     			   display(rs);
                } catch (SQLException e) {

                }
            }
			else
			{
				//do nothing
			}
    }

    //display function for member and author based on particular selection criteria of papers
	public void display(ResultSet rs)
    {
        //display the contents of the paper selected
        try
        {
            while (rs.next())
         {
        String title = rs.getString(1);
        String category = rs.getString(2);
        String confName = rs.getString(3);
        String abstracts = rs.getString(4);
        Date pubDate = rs.getDate(5);
        int authorID = rs.getInt(6);
        String authorName = rs.getString(7);
        String locConf = rs.getString(8);

        System.out.println(" ");
        System.out.println(" Title :  "+title);
        System.out.println(" Category :  "+category);
        System.out.println(" Conference Name :  "+confName);
        System.out.println(" ");
        System.out.println(" Abstract :  "+abstracts);
        System.out.println(" ");
        System.out.println(" Publication Date :  "+pubDate.toString());
        System.out.println(" Author ID :  "+authorID);
        System.out.println(" Author Name :  "+authorName);
        System.out.println(" Location of conference :  "+locConf);
        System.out.println(" **************************************************************");
        System.out.println(" ");
         }

        }
        catch (Exception e)
       {
        System.out.println("Exception is " + e);
      }

    }

	//author function (user functionality of database)
	public void publisher(int a, Connection con)
    {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String choice = "";
        Scanner in = new Scanner(System.in);
        System.out.println(" ");
        System.out.println("Eneter any of the options below : ");
        System.out.println("1. Add a Conference paper ");
        System.out.println("2. Modify the paper entered ");
        choice = in.nextLine();

     //for adding paper by publisher
    if(choice.equals("1"))
     {
    	try{
            String entry = "";
            String Query = "select * from Author where authorID = "+a;
            stmt = con.prepareStatement(Query);
			rs = stmt.executeQuery(Query);
            String name = "";
            while(rs.next())
            {
            int authorID = rs.getInt(1);
            name = rs.getString(2);
            String instu = rs.getString(3);
            String emailAddr = rs.getString(4);
            }

            System.out.println(" ");
            System.out.println("Enter the following values seperated by a comma :");
            System.out.println("Title, category, Conference name, abstract, Publication date, Location of conference ");
            System.out.println(" Enter date in the following format : YYYY-MM-DD ");
            entry = in.nextLine();
            String entryArr[] = new String[6];
            entryArr = entry.split(",");

             String insert = "insert into Conference_Paper (Title, category, conference_name, abstract, "+
             "publication_date, authorID, authorName, locationOfConf) VALUES (?,?,?,?,?,?,?,?)";
             stmt = con.prepareStatement(insert);
             stmt.setString(1, entryArr[0]);
             stmt.setString(2, entryArr[1]);
             stmt.setString(3, entryArr[2]);
             stmt.setString(4, entryArr[3]);
             stmt.setString(5, entryArr[4]);
             stmt.setInt(6, a);
             stmt.setString(7, name);
             stmt.setString(8, entryArr[5]);
             stmt.executeUpdate();

             //insertion in publishedBy table
             Query = "select * from publishedBy where authorID = "+a;
             String title = "";
             stmt = con.prepareStatement(Query);
            // stmt.setInt(1, a);
          	 rs = stmt.executeQuery(Query);

          	 //if author already exists in the publishedBy table
          	 if(rs.next())
          		 {
          			title = rs.getString(2);
          			title = title.concat(" , ");
                    title = title.concat(entryArr[0]);
                    Query = "update publishedBy set Title = ? where authorID = ? ";
                   // System.out.println(" query is :"+Query);
                    stmt = con.prepareStatement(Query);
                    stmt.setString(1, title);
                    stmt.setInt(2, a);
                    stmt.executeUpdate();
          		 }

          	 //if new author makes entry
          		 else
          		 {
          			title = title.concat(entryArr[0]);
          			Query = "insert into publishedBy (authorID, Title) VALUES (?,?)";
          			//System.out.println(" query is :"+Query);
          			stmt = con.prepareStatement(Query);
          			stmt.setInt(1, a);
                    stmt.setString(2, title);
                    stmt.executeUpdate();
          		 }
                     System.out.println(" Conference paper added to publishedBy table ");
                     Query = "select * from Conference_Paper where authorID = "+a;
                     stmt = con.prepareStatement(Query);
                  	 rs = stmt.executeQuery(Query);

    	}
                   catch (SQLException e1) {
                        e1.printStackTrace();
                    }
    	   System.out.println(" ");
           display(rs);
     }


    //for modifying paper by publisher (only on category / abstract )
    //for other modifications, he needs to consult the admin
    else if(choice.equals("2"))
            {
          String entry = "";
          String Query = "";
          String cat = "";
          String abstracts = "";
          String oldCat = "";
          String oldAbstract = "";

          String pTitle = "";
          int pID;

          try
          {
          Query = "select * from Conference_Paper where authorID = "+a;
          stmt = con.prepareStatement(Query);
         // stmt.setInt(1, a);
		  rs = stmt.executeQuery(Query);
          display(rs);

            System.out.println(" Enter the title of the paper you want to modify (enter exact name) followed by the new category and abstract ");
            System.out.println(" Enter values seperated by commas. If you want category or abstract to be unchanged input value nill ");
            System.out.println(" Enter in the following order ");
            System.out.println("Title, category, abstract");

            entry = in.nextLine();
            String entryArr[] = new String[3];
            entryArr = entry.split(",");

            //check to see if the author is modifying his own paper only
            boolean flag = false;
            while(rs.next())
            {
            	if(rs.getString(1).equalsIgnoreCase(entryArr[0]))
            		flag = true;
            	else
            		flag = false;

            }

            if(!flag){
            	Query = "select * from Conference_Paper where Title = '"+entryArr[0]+"';";
                stmt = con.prepareStatement(Query);
               // stmt.setString(1, entryArr[0]);
    			rs = stmt.executeQuery(Query);

    			while(rs.next())
    			{
    			  oldCat = rs.getString(2);
    			  oldAbstract = rs.getString(4);

    			}
    			if(entryArr[1].equals("nil"))
    			{
    				cat = oldCat;
    			}
    			else
    			{
    				cat = entryArr[1];
    			}
    			if(entryArr[2].equals("nil"))
    			{
    				abstracts = oldAbstract;
    			}
    			else
    			{
    				abstracts = entryArr[2];
    			}
				Query = "update Conference_Paper set category = ?, abstract = ? where Title = ? ";
				stmt = con.prepareStatement(Query);
                stmt.setString(1, entryArr[1]);
                stmt.setString(2, entryArr[2]);
                stmt.setString(3, entryArr[0]);
                stmt.executeUpdate();

            Query = " select * from Conference_Paper where authorID = ?";
            stmt = con.prepareStatement(Query);
            stmt.setInt(1, a);
            rs = stmt.executeQuery();
            System.out.println(" ");
            display(rs);
            }

            //if an author tries to modify someone else's paper
            else
            {
            	System.out.println("You are not authorized to modify this paper");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


     }
    else
    {
    	//do nothing
    }

    }

	//administrator function
	public void administrator(int a, Connection con)
    {
        Scanner in = new Scanner(System.in);
        String choice = "";
        System.out.println(" ");
        System.out.println("Enter the operation you wish to carry out : ");
        System.out.println("1.  Display Members list ");
        System.out.println("2.  Add a new member ");
        System.out.println("3.  Delete a member ");
        System.out.println("4.  Display Publishers list ");
        System.out.println("5.  Add a publisher ");
        System.out.println("6.  Display Conference Papers list ");
        System.out.println("7.  Add a Conference Paper ");
        System.out.println("8.  Delete a Conference Paper ");
        System.out.println("9.  Display publishedBy table ");
        System.out.println("10. Modify a member details ");
        System.out.println("11. Modify an author details ");
        System.out.println("12. Modify conference paper details ");
        System.out.println(" ************************************************************");
        System.out.println(" ");

       choice = in.nextLine();
       if(choice.equals("1"))
       {
                memberDisplay(con);
       }
       else if(choice.equals("2"))
       {
                addMember(con);

       }
       else if(choice.equals("3"))
       {
                deleteMember(con);
       }
       else if(choice.equals("4"))
       {
                publisherDisp(con);
       }
       else if(choice.equals("5"))
       {
                addPublisher(con);
       }
       else if(choice.equals("6"))
       {
                confTableDisp(con);
       }
       else if(choice.equals("7"))
       {
    	     confTableAdd(con);
       }
       else if(choice.equals("8"))
       {
    	   confTableDelete(con);
       }
       else if(choice.equals("9"))
       {
    	   publishedByDisp(con);
       }
       else if(choice.equals("10"))
       {
    	   modifyMember(con);
       }
       else if(choice.equals("11"))
       {
    	   modifyAuthor(con);
       }
       else if(choice.equals("12"))
       {
    	   modifyConfPaper(con);
       }
    }

	//display members list in the database function
    public void memberDisplay(Connection con)
    {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        String Query = "select * from Member";
        stmt = con.prepareStatement(Query);
		rs = stmt.executeQuery(Query);

        while(rs.next())
        {
        int memberID = rs.getInt(1);
        String name = rs.getString(2);
        String instu = rs.getString(3);
        String emailAddr = rs.getString(4);
        //String catView = rs.getString(5);
        System.out.println("Member ID : "+memberID);
        System.out.println("Member Name: "+name);
        System.out.println("Institution : "+instu);
        System.out.println("Email address : "+emailAddr);
        System.out.println(" **************************************************************");
        }
        }
        catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
    }

    //add a member to the DB
    public void addMember(Connection con)
    {
    	ResultSet rs = null;
        PreparedStatement stmt = null;
       Scanner in = new Scanner(System.in);
       String entry = "";
       System.out.println("Enter the following values seperated by a comma :");
       System.out.println("Name, Institution, Email address");
       entry = in.nextLine();
            String entryArr[] = new String[3];
            entryArr = entry.split(",");
       try {
       String Query = "select memberID from Member";
       stmt = con.prepareStatement(Query);
	   rs = stmt.executeQuery(Query);
       int id = 0;
       while(rs.next())
        {
            id = rs.getInt(1);
        }
        id = id+1; //add next no as entry in member table
        String insert = " insert into Member (memberID, name, institution, email_addr) " +
        		"VALUES (?,?,?,?)";
        stmt = con.prepareStatement(insert);
        stmt.setInt(1, id);
        stmt.setString(2, entryArr[0]);
        stmt.setString(3, entryArr[1]);
        stmt.setString(4, entryArr[2]);
        stmt.executeUpdate();
       }
       catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
        System.out.println("Member successfully added ***************** ");
        memberDisplay(con);
    }


    //delete a member from the DB
    public void deleteMember(Connection con)
    {
    	try {
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
        Scanner in = new Scanner(System.in);
        int id;
        System.out.println("Enter memberID of Member to be deleted :");
        id = in.nextInt();
        String delete = "delete from Member where memberID = ?";
        stmt = con.prepareStatement(delete);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    	}
    	 catch(Exception e)
         {
             System.out.println(" Exception : "+e);
         }
        System.out.println("Member successfully deleted ***************** ");
        memberDisplay(con);
    }


    //Display the author / publishers list in the DB
    public void publisherDisp(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        String Query = "select * from Author";
        stmt = con.prepareStatement(Query);
 	    rs = stmt.executeQuery(Query);
        while(rs.next())
        {
        int authorID = rs.getInt(1);
        String name = rs.getString(2);
        String instu = rs.getString(3);
        String emailAddr = rs.getString(4);
        System.out.println("Author ID : "+authorID);
        System.out.println("Member Name: "+name);
        System.out.println("Institution : "+instu);
        System.out.println("Email address : "+emailAddr);
        System.out.println(" **************************************************************");
        }
        }
        catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
    }

    //add a publisher to the DB
    public void addPublisher(Connection con)
    {
      PreparedStatement stmt = null;
      ResultSet rs = null;
       Scanner in = new Scanner(System.in);
       String entry = "";
       System.out.println("Enter the following values seperated by a comma :");
       System.out.println("Name, Institution, Email address"); //category viewed ???????
       entry = in.nextLine();
            String entryArr[] = new String[4];
            entryArr = entry.split(",");
       try {
       String Query = "select authorID from Author";
       stmt = con.prepareStatement(Query);
	   rs = stmt.executeQuery(Query);
       int id = 0;
       while(rs.next())
        {
            id = rs.getInt(1);
        }
        id = id+1; //add next no as entry in member table
        String insert = "insert into Author (authorID, authorName, institution, email_addr) VALUES (?, ?, ?, ?)";
        stmt = con.prepareStatement(insert);
        stmt.setInt(1, id);
        stmt.setString(2, entryArr[0]);
        stmt.setString(3, entryArr[1]);
        stmt.setString(4, entryArr[2]);
        stmt.executeUpdate();
       }
       catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
        System.out.println("Publisher successfully added ***************** ");
        publisherDisp(con);
    }


    //comference papers display
    public void confTableDisp(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        String Query = "select * from Conference_Paper";
        stmt = con.prepareStatement(Query);
        rs = stmt.executeQuery(Query);
        while(rs.next())
        {
            String title = rs.getString(1);
        String category = rs.getString(2);
        String confName = rs.getString(3);
        String abstracts = rs.getString(4);
        Date pubDate = rs.getDate(5);
        int authorID = rs.getInt(6);
        String authorName = rs.getString(7);
        String locConf = rs.getString(8);

        System.out.println(" Title :  "+title);
        System.out.println(" Category :  "+category);
        System.out.println(" Conference Name :  "+confName);
        System.out.println(" ");
        System.out.println(" Abstract :  "+abstracts);
        System.out.println(" ");
        System.out.println(" Publication Date :  "+pubDate.toString());
        System.out.println(" Author ID :  "+authorID);
        System.out.println(" Author Name :  "+authorName);
        System.out.println(" Location of conference :  "+locConf);
        System.out.println(" **************************************************************");
        System.out.println(" ");
        }
        }
        catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
    }

    //publishedBy display
    public void publishedByDisp(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        String Query = "select * from publishedBy";
        stmt = con.prepareStatement(Query);
        rs = stmt.executeQuery(Query);
        while(rs.next())
        {
        	int memberID = rs.getInt(1);
            String title = rs.getString(2);
            System.out.println(" Author ID :"+memberID);
            System.out.println(" Papers published :"+title);
            System.out.println(" **************************************************************");
            System.out.println(" ");
        }
        }
        catch(Exception e)
        {
        	System.out.println("Exception : "+e);
        }

    }

    //add conference paper
    public void confTableAdd(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String entry = "";
        Scanner in = new Scanner(System.in);
        try {
        	System.out.println("Enter the following values seperated by a comma :");
            System.out.println("Title, category, Conference name, abstract, Publication date, authorID, author name, Location of conference");
            System.out.println(" Enter date in the following format : YYYY-MM-DD ");
            entry = in.nextLine();
            String entryArr[] = new String[7];
            entryArr = entry.split(",");

            int a = Integer.parseInt(entryArr[5]);
             String insert = "insert into Conference_Paper (Title, category, conference_name, abstract, "+
             "publication_date, authorID, authorName, locationOfConf) VALUES (?,?,?,?,?,?,?,?)";
             stmt = con.prepareStatement(insert);
             stmt.setString(1, entryArr[0]);
             stmt.setString(2, entryArr[1]);
             stmt.setString(3, entryArr[2]);
             stmt.setString(4, entryArr[3]);
             stmt.setString(5, entryArr[4]);
             stmt.setInt(6, a);
             stmt.setString(7, entryArr[5]);
             stmt.setString(8, entryArr[6]);
             stmt.executeUpdate();

             System.out.println(" Conference paper added to conference_paper table ");

             //to add paper to publishedBy
             String Query = "select * from publishedBy where authorID = "+a;
             String title = "";
             stmt = con.prepareStatement(Query);
          	 rs = stmt.executeQuery(Query);

          	 if(rs.next())
          		 {
          			title = rs.getString(2);
          			title = title.concat(" , ");
                    title = title.concat(entryArr[0]);
                    Query = "update publishedBy set Title = ? where authorID = ? ";
                    System.out.println(" query is :"+Query);
                    stmt = con.prepareStatement(Query);
                    stmt.setString(1, title);
                    stmt.setInt(2, a);
                    stmt.executeUpdate();
          		 }
          		 else
          		 {
          			title = title.concat(entryArr[0]);
          			Query = "insert into publishedBy (authorID, Title) VALUES (?,?)";
          			System.out.println(" query is :"+Query);
          			stmt = con.prepareStatement(Query);
          			stmt.setInt(1, a);
                    stmt.setString(2, title);
                    stmt.executeUpdate();
          		 }
                     System.out.println(" Conference paper added to publishedBy table ");

        }
        catch(Exception e)
        {
        	System.out.println("Exception is "+e);
        }
    }

    //delete conference papers from DB
    public void confTableDelete(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String entry = "";
        Scanner in = new Scanner(System.in);
    	try
    	{
        System.out.println("Enter title of the paper to be deleted (exactly ) :");
        entry = in.nextLine();
        String delete = "delete from Conference_Paper where Title = ?";
        stmt = con.prepareStatement(delete);
        stmt.setString(1, entry);
        stmt.executeUpdate();

    	}
    	 catch(Exception e)
         {
             System.out.println(" Exception : "+e);
         }

        System.out.println("Conference Paper successfully deleted ***************** ");
    }

    //modify member
    public void modifyMember(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String entry = "";
        String oldName = "";
        String oldInst = "";
        String oldEmail = "";
        String name = "";
        String inst = "";
        String email = "";

        int a = 0;
        String m = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Enter memberID to be modified");
        m = in.nextLine();
        a = Integer.parseInt(m);
        System.out.println("Enter the fields to be modified in the following order, seperated by commas..");
        System.out.println(" Name, institution, email addres ");
        System.out.println("If no modification required for a particular field, enter nill");
        entry = in.nextLine();
        String entryArr[] = new String[3];
        entryArr = entry.split(",");
    	try
    	{
     		    String Query = "select * from Member where memberID = ?";
    	        stmt = con.prepareStatement(Query);
    	        stmt.setInt(1, a);
    			rs = stmt.executeQuery();
    			while(rs.next())
    			{
    				oldName = rs.getString(2);
    				oldInst = rs.getString(3);
    				oldEmail = rs.getString(4);
    			}
    			if(entryArr[0].equals("nil"))
    			{
    				name = oldName;
    			}
    			else
    			{
    				name = entryArr[0];
    			}
    			if(entryArr[1].equals("nil"))
    			{
    				inst = oldInst;
    			}
    			else
    			{
    			    inst = entryArr[1];
    			}
    			if(entryArr[2].equals("nil"))
    			{
    				email = oldEmail;
    			}
    			else
    			{
    				email = entryArr[2];
    			}
    			String update = "update Member set name = ?,  institution = ?, email_addr = ? where memberID = ?";
    			stmt = con.prepareStatement(update);
    			stmt.setString(1, name);
    			stmt.setString(2, inst);
    			stmt.setString(3, email);
    			stmt.setInt(4, a);
    			stmt.executeUpdate();
    	}
    	catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
    	System.out.println(" ");
    	System.out.println("MemberID "+a+" successfully modified ***************** ");
    	memberDisplay(con);
    }

    //modify author
    public void modifyAuthor(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String entry = "";
        String oldName = "";
        String oldInst = "";
        String oldEmail = "";
        String name = "";
        String inst = "";
        String email = "";

        int a = 0;
        String m = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Enter authorID to be modified");
        m = in.nextLine();
        a = Integer.parseInt(m);
        System.out.println("Enter the fields to be modified in the following order, seperated by commas..");
        System.out.println(" Name, institution, email addres ");
        System.out.println("If no modification required for a particular field, enter nill");
        entry = in.nextLine();
        String entryArr[] = new String[3];
        entryArr = entry.split(",");
    	try
    	{
    		String Query = "select * from Author where authorID = "+a;
 	        stmt = con.prepareStatement(Query);
 	       // stmt.setInt(1, a);
 			rs = stmt.executeQuery(Query);
 			while(rs.next())
 			{
 				oldName = rs.getString(2);
 				oldInst = rs.getString(3);
 				oldEmail = rs.getString(4);
 			}
 			if(entryArr[0].equals("nil"))
 			{
 				name = oldName;
 			}
 			else
 			{
 				name = entryArr[0];
 			}
 			if(entryArr[1].equals("nil"))
 			{
 				inst = oldInst;
 			}
 			else
 			{
 			    inst = entryArr[1];
 			}
 			if(entryArr[2].equals("nil"))
 			{
 				email = oldEmail;
 			}
 			else
 			{
 				email = entryArr[2];
 			}
 			String update = "update Author set authorName = ?,  institution = ?, email_addr = ? where authorID = ?";
 			stmt = con.prepareStatement(update);
 			stmt.setString(1, name);
 			stmt.setString(2, inst);
 			stmt.setString(3, email);
 			stmt.setInt(4, a);
 			stmt.executeUpdate();

    	}
    	catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }
    	System.out.println(" ");
    	System.out.println("AuthorID "+a+" successfully modified ***************** ");
    	publisherDisp(con);
    }

    //modify conference paper
    public void modifyConfPaper(Connection con)
    {
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String entry = "";
        String title = "";
        String cat = "";
        String confName = "";
        String abstracts = "";
        String date = "";
        int authorID;
        String authorName = "";
        String locOfConf = "";
        String oldCat = "";
        String oldConfName = "";
        String oldAbstracts = "";
        String oldDate = "";
        int oldAuthorID = 0;
        String oldAuthorName = "";
        String oldLocOfConf = "";

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the title of the paper (exactly) to be modified");
        title = in.nextLine();
        System.out.println(" ");
        System.out.println("Enter the fields to be modified in the following order, seperated by commas..");
        System.out.println("Category, Conference name, Abstract, Publication Date, authorID, author Name, Location of Conference ");
        System.out.println("Enter date in the following format : YYYY-MM_DD ");
        System.out.println("If no modification required for a particular field, enter nill");
        System.out.println(" ");
        entry = in.nextLine();
        String entryArr[] = new String[7];
        entryArr = entry.split(",");
    	try
    	{
    		String Query = "select * from Conference_Paper where Title = ?";
 	        stmt = con.prepareStatement(Query);
 	        stmt.setString(1, title);
 			rs = stmt.executeQuery();
 			while(rs.next())
 			{
 				oldCat = rs.getString(2);
 				oldConfName = rs.getString(3);
 				oldAbstracts = rs.getString(4);
 				oldDate = rs.getString(5);
 				oldAuthorID = rs.getInt(6);
 				oldAuthorName = rs.getString(7);
 				oldLocOfConf = rs.getString(8);
 			}
 			if(entryArr[0].equals("nil"))
 			{
 				cat = oldCat;
 			}
 			else
 			{
 				cat = entryArr[0];
 			}
 			if(entryArr[1].equals("nil"))
 			{
 				confName = oldConfName;
 			}
 			else
 			{
 				confName = entryArr[1];
 			}
 			if(entryArr[2].equals("nil"))
 			{
 				abstracts = oldAbstracts;
 			}
 			else
 			{
 				abstracts = entryArr[2];
 			}
 			if(entryArr[3].equals("nil"))
 			{
 				date = oldDate;
 			}
 			else
 			{
 				date = entryArr[3];
 			}
 			if(entryArr[4].equals("nil"))
 			{
 				authorID = oldAuthorID;
 			}
 			else
 			{
 				int a = Integer.parseInt(entryArr[4]);
 				authorID = a;
 			}
 			if(entryArr[5].equals("nil"))
 			{
 				authorName = oldAuthorName;
 			}
 			else
 			{
 				authorName = entryArr[5];
 			}
 			if(entryArr[5].equals("nil"))
 			{
 				locOfConf = oldLocOfConf;
 			}
 			else
 			{
 				locOfConf = entryArr[6];
 			}
 			String update = "update Conference_Paper set category = ?,  conference_name = ?, abstract = ?, publication_date = ?," +
 					"authorID = ?, authorName = ?, locationOfConf = ? where Title = ?";
 			stmt = con.prepareStatement(update);
 			stmt.setString(1, cat);
 			stmt.setString(2, confName);
 			stmt.setString(3, abstracts);
 			stmt.setString(4, date);
 			stmt.setInt(5, authorID);
 			stmt.setString(6, authorName);
 			stmt.setString(7, locOfConf);
 			stmt.setString(8, title);
 			stmt.executeUpdate();

 			System.out.println("Conference paper with title "+title+" successfully modified ***************** ");
 	        Query = "select * from Conference_Paper where Title = ?";
 		    stmt = con.prepareStatement(Query);
 		    stmt.setString(1, title);
 		    rs = stmt.executeQuery();
    	}
    	catch(Exception e)
        {
            System.out.println(" Exception : "+e);
        }

    	display(rs);
    }

    //main function
    public static void main(String args[])
    {
    	Connection con = null;
        dataBase db = new dataBase();
        Statement stmt = null;
        int a = 0;
		int option = 1;
        try
        {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpata","jpata", "jpata4836");

        int count = 0;
        Scanner in = new Scanner(System.in);
        System.out.println(" HELLO WELCOME TO CONFERENCE PAPERS DATABASE ");
        do
        {
        	count++;
        System.out.println(" PLEASE ENTER YOUR LOGIN ID ");
        a = in.nextInt();
        System.out.println(" ");
		System.out.println("Login ID entered is "+a);

        ResultSet rs = null;
        stmt = con.prepareStatement("select memberID from Member");
        rs = stmt.executeQuery("select memberID from Member");

		while(rs.next())
        {
        if(a == rs.getInt(1))
        {
            option = 2;
            count = 3;
        }
        }

		stmt = con.prepareStatement("select adminID from Administrator");
        rs = stmt.executeQuery("select adminID from Administrator");
        while(rs.next())
        {
        	if(a == rs.getInt(1))
        {
            option = 3;
            count = 3;
        }
        }

        stmt = con.prepareStatement("select authorID from Author");
        rs = stmt.executeQuery("select authorID from Author");
         while(rs.next())
        {
        if (a == rs.getInt(1))
        {
           option = 4;
           count = 3;
        }
        }
        }
        while(count <3); //will allow user to input the right login id thrice

     }

   catch (Exception e)
   {
     System.out.println("Exception is " + e);
   }

        switch(option)
        {
        case 1:
              System.out.println(" Not a vlaid LoginID");
        break;
        case 2:
            System.out.println(" Logging in as a Member " + a);
           db.member(a,con);
            break;
        case 4:
           System.out.println("Logging in as an Author / Publisher " + a);
         db.publisher(a,con);
        break;

        case 3:
             System.out.println(" Logging in as an administrator " + a);
           db.administrator(a,con);
             break;

        }
        System.out.println("****************************************************");
        System.out.println(" Thank you for using our database ");
        System.out.println("****************************************************");
        try{
        con.close();
        }
        catch (Exception e)
        {
          System.out.println("Exception is " + e);
        }
    }
}
