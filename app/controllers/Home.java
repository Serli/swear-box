package controllers;


import models.Person;

import org.bson.types.ObjectId;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.Play;
import play.mvc.Result;
import views.html.index;

import com.cloudinary.Cloudinary;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.fasterxml.jackson.databind.JsonNode;

import dao.ConsumerDAO;
import dao.PersonDAO;
import dao.StatisticsDAO;

/**
 * Managed actions for Home view
 * @author Geoffrey
 *
 */
@Singleton
public class Home extends JavaController {

	private static final Integer USER_PAGE = 0;
	private static final Integer STATISTICS_PAGE = 1;
	private static final Integer ADMIN_PAGE = 2;
	private static final Integer BACKOFFICE_PAGE = 3;
	private static final Integer HELP_PAGE = 4;

	private static final Integer NOT_CONNECTED = 0;
	private static final Integer REGULAR = 1;
	private static final Integer ADMINISTRATOR = 2;

	@Inject
	private ConsumerDAO consumerDAO;

	@Inject
	private PersonDAO personDAO;

	@Inject
	private StatisticsDAO statisticsDAO;

	private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

	/**
	 * Action called when launching the app
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	public Result index() {
		if(!isConnected()) {
			final String urlGoogle = getRedirectAction("Google2Client").getLocation();
			return ok(index.render(urlGoogle, getStatus(), USER_PAGE));
		} else {
			if(blackLister()) {
				return redirect(routes.Home.error());
			} else {
				return redirect(routes.Home.user());
			}
		}
	}

	/**
	 * Action called for displaying the user view
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result user() {
		//Get the name in the user google profile
		Google2Profile googleProfile = (Google2Profile) getUserProfile();

		//Add the user to the data if it is needed
		String firstname = googleProfile.getFirstName();
		String email = googleProfile.getEmail();

		if(consumerDAO.add(email)){
			//Add a member with the name and the firstname of the user

			String url = cloudinary.url().format("png")
					.generate(Play.application().configuration().getString("AvatarDefault"));
			String picture = url.replace("http", "https");
			String name = googleProfile.getFamilyName();
			Person person = new Person(ObjectId.get().toString(),name,firstname,0,picture);
			personDAO.add(person,email);
		}

		if(blackLister()){
			return redirect(routes.Home.error());
		}
		return ok(views.html.user.render(firstname, getStatus(), USER_PAGE));
	}

	public Result error() {
		org.pac4j.play.CallbackController.logoutAndRedirect();
		return ok(views.html.error.render());
	}

	/**
	 * Action called for increase a person debt
	 * @return Result : fonction result, Ok|pb
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result increaseDebt(String id) {
		if(!blackLister()){
			Google2Profile googleProfile = (Google2Profile) getUserProfile();
			String email = googleProfile.getEmail();
			personDAO.incrementDebt(id,email);
			statisticsDAO.add(id, email);
			return ok();
			}
		return ok(views.html.error.render());
	}

	/**
	 * Method's using in conf\routes and to display the admin page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result admin() {

		if(!blackLister()){
			return ok(views.html.admin.render(getStatus(), ADMIN_PAGE));
		}
		return redirect(routes.Home.error());

	}

	/**
	 * Method's using in conf\routes and to display the help page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	public Result help() {
		return ok(views.html.help.render(getStatus(), HELP_PAGE));
	}

	/**
	 * Method's using in conf\routes and to display the statistics page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result statistics() {

		if(!blackLister()){
			return ok(views.html.statistics.render(getStatus(), STATISTICS_PAGE));
		}
		return redirect(routes.Home.error());
	}

	/**
	 * Method's using in conf\routes and to display the backoffice page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result backoffice() {

		if(!blackLister()){
			return ok(views.html.backoffice.render(getStatus(), BACKOFFICE_PAGE));
		}
		return redirect(routes.Home.error());
	}

	/**
	 * Method's using in conf\routes and to display the statisticsbackoffice page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result statisticsBackoffice() {

		if(!blackLister()){
			return ok(views.html.statisticsbackoffice.render(getStatus(), BACKOFFICE_PAGE));
		}
		return redirect(routes.Home.error());
	}

	/**
	 * Method's using in conf\routes and to display the userbackoffice page
	 * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result userBackoffice(String consumer) {

		if(!blackLister()){
			return ok(views.html.userbackoffice.render(getStatus(), BACKOFFICE_PAGE));
		}
		return redirect(routes.Home.error());
	}

	private Integer getStatus() {
		Integer result;
		if(!isConnected()) {
			result = NOT_CONNECTED;
		} else {
			String email = getUserProfile().getEmail();
			JsonNode j = consumerDAO.findOne(email);
			Boolean admin = j.findValue("admin").asBoolean();
			if(admin) {
				result = ADMINISTRATOR;
			} else {
				result = REGULAR;
			}
		}
		return result;
	}

	private static Boolean isConnected() {
		return !(getUserProfile() == null);
	}

	private Boolean blackLister(){
		String email = getUserProfile().getEmail();
		return consumerDAO.inBlackLister(email);
	}

}