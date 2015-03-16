package controllers;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;
import dao.StatisticsDAO;

/**
 * Managed actions for administration view
 */
@Singleton
public class Backoffice extends JavaController {

	@Inject
	private ConsumerDAO consumerDAO;

	@Inject
	private StatisticsDAO statisticsDAO;

	/**
	 * return all Consumers
	 * @return Result(JSON) : list of consumers
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result findAll() {
		if(isAuthorized()){
			return ok(consumerDAO.findAll());
		}
		return badRequest();
	}

	/**
	 * return a Consumers
	 * @param String : consumer's id
	 * @return Result(JSON) : a single consumers
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result findOne(String email) {
		if(isAuthorized()){
			return ok(consumerDAO.findOne(email));
		}
		return badRequest();
	}

	/**
	 * set a Consumers to admin
	 * @param String : consumer's id
	 * @return Result : fonction result, Ok|pb
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result setAdmin(String email) {
		if(isAuthorized()){
			String admin = getUserProfile().getEmail();
			if(consumerDAO.setAdmin(admin, email)){
				return ok();
			}
		}
		return badRequest();
	}

	/**
	 * set a Consumers to blacklist
	 * @param String : consumer's id
	 * @return Result : fonction result, Ok|pb
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result setBlacklisted(String email) {
		if(isAuthorized()){
			String admin = getUserProfile().getEmail();
			if(consumerDAO.setBlacklisted(admin, email,true)){
				return ok();
			}
		}
		return badRequest();
	}

	/**
	 * unset a Consumers to blacklist
	 * @param String : consumer's id
	 * @return Result : fonction result, Ok|pb
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result unsetBlacklisted(String email) {
		if(isAuthorized()){
			String admin = getUserProfile().getEmail();
			if(consumerDAO.setBlacklisted(admin, email,false)){
				return ok();
			}
		}
		return badRequest();
	}

	/**
	 * get all stats
	 * @return Result(JSON) : stats
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result list() {
		if(isAuthorized()){
			return ok(statisticsDAO.list());
		}
		return badRequest();
	}

	/**
	 * get specifics stats
	 * @return Result(JSON) : stats
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result someStats() {
		if(isAuthorized()){
			return ok(statisticsDAO.someStats());
		}
		return badRequest();
	}

	/**
	 * get authorization
	 * @return boolean : true = access; false = denied
	 */
	private boolean isAuthorized(){
		String email = getUserProfile().getEmail();
		return consumerDAO.isAdmin(email);
	}

}
