package com.harsha;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class FeedSystemController {

	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<String> userData, feedData,userMailId;
	UserDetails userDetails;

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updates(HttpServletResponse response, HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		System.out.println("Username to display:" + userData.get(0));
		session.setAttribute("name", userData.get(0));
		session.setAttribute("mail", userData.get(1));
		System.out.println(userData);
		return new ModelAndView("update","userName",userData.get(0));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public void getUsers(HttpServletResponse response) throws IOException {
		Query q = pm.newQuery(UserDetails.class);
		try{
			q.setOrdering("signUpUserName asc");
			List<UserDetails> results = null;
				results = (List<UserDetails>) q.execute();
				if (!results.isEmpty()) {
					System.out.println(results);
					response.getWriter().write(new Gson().toJson(results));
				}
		}
		finally
		{
			q.closeAll();
		}
	}
	@RequestMapping(value = "/signupData", method = RequestMethod.GET)
	public ModelAndView signUpData() {
		System.out.println(userDetails.getSignUpUserName());
		return new ModelAndView("signup", "name", userDetails.getSignUpUserName());
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println(userName);
		System.out.println(password);
		Login login = new Login();
		List<String> userData = data(userName);
		System.out.println("userdata:" + userData);
		if (userData.contains(userName) && userData.contains(password)) {
			login.setUserName(userName);
			login.setPassword(password);
			HttpSession session = request.getSession();
			session.setAttribute("name", userData.get(0));
			response.getWriter().write(new Gson().toJson("false"));
		} else {
			response.getWriter().write(new Gson().toJson(userName));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateservlet", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String userName1 = (String)request.getSession().getAttribute("name");
		System.out.println(userName1);
		if (null == userName1) {
		   request.setAttribute("Error", "Session has ended.  Please login.");
		  return new ModelAndView("signup");
		}
		
		String feedText = request.getParameter("feed");
		String userName = request.getParameter("userName");
		String userMail = request.getParameter("userMail");
		String completeUserName = userName.substring(8);
		System.out.println("Complete UserName:" + completeUserName);
		System.out.println("userMail:"+userMail.substring(11));
		long millis = System.currentTimeMillis();
		UpdateFeed updateFeed = new UpdateFeed();
		if (!feedText.equals("")) {
			updateFeed.setFeed(feedText);
			updateFeed.setUserMail(userMail);
			updateFeed.setDate(millis);
			updateFeed.setUserName(completeUserName);
			System.out.println("userMail:"+updateFeed.getUserMail());
			pm.makePersistent(updateFeed);
			String userNameToDisplay = new Gson().toJson(completeUserName);
			System.out.println("UserNameTDisplay:" + userNameToDisplay);
			String feedToDisplay = new Gson().toJson(feedText);
			System.out.println("Feed To display:" + feedToDisplay);
			String dateToDisplay = new Gson().toJson(millis);
			String jsonObjects = "[" + userNameToDisplay + "," + feedToDisplay + "," + dateToDisplay + "]";//creating json array
			response.getWriter().write(jsonObjects);//sending response as json
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String signUpUserName = request.getParameter("userName");
		String signUpPassword = request.getParameter("password");
		String signUpConfirmPassword = request.getParameter("confirmPassword");
		String signUpEmail = request.getParameter("email");

		byte[] message = signUpPassword.getBytes("UTF-8");
		String encoded = DatatypeConverter.printBase64Binary(message);
		byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);

		System.out.println(encoded);
		System.out.println(new String(decoded, "UTF-8"));

		int index = signUpEmail.indexOf("@");
		int dot = signUpEmail.lastIndexOf(".");

		userDetails = new UserDetails();
		if (!signUpUserName.equals("") && !signUpPassword.equals("") && (signUpPassword.length() >= 6)
				&& signUpConfirmPassword.equals(signUpPassword) && index > 1 && dot > index + 2
				&& dot + 2 < signUpEmail.length()) {
			userDetails.setSignUpUserName(signUpUserName);
			userDetails.setSignUpPassword(encoded);
			userDetails.setSignUpEmail(signUpEmail);
			userDetails.setIsDelete(false);
			userDetails.setSource("default");
			long millis;
			userDetails.setDate(millis = System.currentTimeMillis());
			List<String> userData = data(userDetails.getSignUpEmail());
			System.out.println(userData);
			if (!userData.contains(signUpEmail)) {
				try {
					pm.makePersistent(userDetails);
				} finally {
					// pm.close();
				}
				response.getWriter().write(new Gson().toJson("false"));
			} else {
				response.getWriter().write(new Gson().toJson(signUpEmail));
			}
		} else {
			response.getWriter().write(new Gson().toJson(signUpEmail));
		}
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
			session.removeAttribute("name");
			session.invalidate();
			return "index";
	}

	@SuppressWarnings({ "unchecked", "null" })
	public List<String> data(String userName) {
		Query q = pm.newQuery(UserDetails.class);
		try{
			q.setFilter("signUpEmail == signUpEmailParam");
			q.setOrdering("date desc");
			q.declareParameters("String signUpEmailParam");
			List<UserDetails> results = null;
			userData = new ArrayList<String>();
			results = (List<UserDetails>) q.execute(userName);
			if (!results.isEmpty() && !(results==null)) {
				for (UserDetails data : results) {
					userData.add(data.getSignUpUserName());
					userData.add(data.getSignUpEmail());
					byte[] decoded = DatatypeConverter.parseBase64Binary(data.getSignUpPassword());
					try {
						userData.add(new String(decoded, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			q.closeAll();
		}
		return userData;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchUpdates")
	public void fetchUpdates(HttpServletResponse response) throws IOException {
		Query q = pm.newQuery(UpdateFeed.class);
		q.setOrdering("date desc");
		List<UpdateFeed> feeds = null;
		try {
			feeds = (List<UpdateFeed>) q.execute();
			System.out.println("Feeds" + feeds);
			if (!(feeds==null) && !feeds.isEmpty()) {
				System.out.println("Feeds: " + feeds);
				response.getWriter().write(new Gson().toJson(feeds));
			}
		} finally {
			q.closeAll();
		}
	}
}