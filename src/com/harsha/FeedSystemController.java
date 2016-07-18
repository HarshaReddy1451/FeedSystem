package com.harsha;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class FeedSystemController {

	PersistenceManager pm = PMF.get().getPersistenceManager();
	@RequestMapping(value="/login",method= RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		
		PrintWriter out= response.getWriter();
		//String pattern= "^[a-zA-Z0-9]*$";
		
		Login login = new Login();
		List<String> userData =data(userName);
		System.out.println(userData);
		if(userData.contains(userName) && userData.contains(password))
		{
			login.setUserName(userName);
			login.setPassword(password);
			return new ModelAndView("update","userName",userData.get(0));
		}
		else
		{
			out.println("<script>alert('UserName or password entered is incorrect')</script>");
		}
		return new ModelAndView("index");
	}
	@RequestMapping(value="/updateservlet",method=RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String feedText = request.getParameter("feed");
		String userName = request.getParameter("userName");
		String completeUserName=userName.substring(8);
		
		System.out.println(completeUserName);
		
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);
		/*DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));*/
		
		String feedObj= new Gson().toJson(feedText);
		String userNameObj=new Gson().toJson(completeUserName);
		String dateToDisplay=new Gson().toJson(date);
		String jsonObjects= "["+userNameObj+","+feedObj+","+dateToDisplay+"]";
		response.getWriter().write(jsonObjects);
		return null;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public ModelAndView signUp(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String signUpUserName = request.getParameter("userName");
		String signUpPassword = request.getParameter("password");
		String signUpConfirmPassword = request.getParameter("confirmPassword");
		String signUpEmail = request.getParameter("email");
		
		
		byte[] message = signUpPassword.getBytes("UTF-8");
		String encoded = DatatypeConverter.printBase64Binary(message);
		byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);

		System.out.println(encoded);
		System.out.println(new String(decoded, "UTF-8"));
		
		PrintWriter out = response.getWriter();
		int index=signUpEmail.indexOf("@");
		int dot=signUpEmail.lastIndexOf(".");
		
		UserDetails userDetails = new UserDetails();
		
		if(!signUpUserName.equals("") && !signUpPassword.equals("") && signUpConfirmPassword.equals(signUpPassword) && index>1 && dot> index+2 && dot+2 < signUpEmail.length())
		{
			userDetails.setSignUpUserName(signUpUserName);
			userDetails.setSignUpPassword(encoded);
			//userDetails.setSignUpConfirmPassword(signUpConfirmPassword);
			userDetails.setSignUpEmail(signUpEmail);
			userDetails.setDate(new Date());
			
			List<String> userData =data(userDetails.getSignUpEmail());
			System.out.println(userData);
			if(!userData.contains(signUpEmail))
			{
				try
				{
					pm.makePersistent(userDetails);
				}
				finally
				{
					//pm.close();
				}
				return new ModelAndView("signup","name",userDetails.getSignUpUserName());
			}
			else
			{
				out.println("<script>alert('User already exists with Info entered.')</script>");
			}
		}
		else
		{
			out.println("<script>alert('Required fields are not appropriate.')</script>");
		}
		return new ModelAndView("index");
	}
	@SuppressWarnings({ "unchecked" })
	public List<String> data(String userName)
	{
		Query q = pm.newQuery(UserDetails.class);
		q.setFilter("signUpEmail == signUpEmailParam");
		q.setOrdering("date desc");
		q.declareParameters("String signUpEmailParam");
		List<UserDetails> results = null;
		List<String> userData= new ArrayList<String>();
		try {
			results = (List<UserDetails>) q.execute(userName);
			if (!results.isEmpty()) {
				// good for listing
				for(UserDetails data : results)
				{
					userData.add(data.getSignUpUserName());
					userData.add(data.getSignUpEmail());
					byte[] decoded=DatatypeConverter.parseBase64Binary(data.getSignUpPassword());
					try {
						userData.add(new String(decoded,"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		} finally {
			q.closeAll();
		}
		return userData;

	}
	
}
