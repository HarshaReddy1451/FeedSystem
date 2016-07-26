package com.harsha;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
	List<String> userData;
	UserDetails userDetails;

	@RequestMapping(value="/getUsers", method=RequestMethod.GET)
	public void getUsers(HttpServletResponse response) throws IOException
	{
		System.out.println(userData.get(0));
		response.getWriter().write(new Gson().toJson(userData.get(0)));
	}
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updates(HttpServletResponse response,HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		session.setAttribute("name",userData.get(0));
		System.out.println(userData);
		return new ModelAndView("update");
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
			response.getWriter().write(new Gson().toJson("false"));
		} else {
			response.getWriter().write(new Gson().toJson(userName));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateservlet", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String feedText = request.getParameter("feed");
		String userName = request.getParameter("userName");
		String completeUserName = userName.substring(11);
		System.out.println(completeUserName);
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);
		UpdateFeed updateFeed = new UpdateFeed();
		if (!feedText.equals("")) {
			updateFeed.setFeed(feedText);
			updateFeed.setUserMail(completeUserName);
			updateFeed.setDate(millis);
			System.out.println(updateFeed.getUserMail());
			pm.makePersistent(updateFeed);
			Query q = pm.newQuery(UpdateFeed.class);
			/*q.setFilter("date == dateParam");
			q.setOrdering("date desc");
			q.declareParameters("String dateParam");*/
			List<UpdateFeed> feeds = null;
			List<String> feedData = new ArrayList<>();
			try {
				feeds = (List<UpdateFeed>) q.execute();
				System.out.println("Feeds"+feeds);
				if (!feeds.isEmpty()) {
					// good for listing
					for (UpdateFeed data : feeds) {
						feedData.add(data.getFeed());
						feedData.add(data.getUserMail());
					}
					System.out.println("Feeds: " + feedData);
					/*Iterator iterator = feedData.iterator();
					while(iterator.hasNext())
					{
						Object element=iterator.next();
						System.out.println(element);
					}*/
					String dateToDisplay = new Gson().toJson(date);
					String feedToDisplay = new Gson().toJson(feedData.get(0));
					String userNameToDisplay = new Gson().toJson(feedData.get(1));
					String jsonObjects = "["+userNameToDisplay+","+feedToDisplay+","+dateToDisplay+"]";
					System.out.println(jsonObjects);
					response.getWriter().write(jsonObjects);
				} else {

				}
			} finally {
				q.closeAll();
			}
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
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
    	if(session != null){
    		session.invalidate();
    	}
		return new ModelAndView("index");
	}

	@SuppressWarnings({ "unchecked", "null" })
	public List<String> data(String userName) {
		Query q = pm.newQuery(UserDetails.class);
		q.setFilter("signUpEmail == signUpEmailParam");
		q.setOrdering("date desc");
		q.declareParameters("String signUpEmailParam");
		List<UserDetails> results = null;
		userData = new ArrayList<String>();
		try {
			results = (List<UserDetails>) q.execute(userName);
			if (!results.isEmpty()) {
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
			} else {

			}
		} finally {
			q.closeAll();
		}
		return userData;
	}
}