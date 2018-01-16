package by.tr.web.kinorating.service.validation;

import by.tr.web.kinorating.domain.Actor;

public class ActorValidator {

	public static boolean validateActor(Actor actor) {
		if (actor == null) {
			return false;
		}
		if (!validateActorFirstName(actor.getFirstName())) {
			return false;
		}
		if (!validateActorAge(actor.getAge())) {
			return false;
		}
		return true;
	}
	
	public static boolean validateActorTranslation(Actor translation) {
		if (translation == null) {
			return false;
		}
		if (!validateActorID(translation.getId())) {
			return false;
		}
		if (!validateActorFirstName(translation.getFirstName())) {
			return false;
		}
		if (!validateActorAge(translation.getAge())) {
			return false;
		}
		return true;
	}
	
	public static boolean validateActorFirstName(String firstName) {
		if (CommonValidator.isEmptyOrNull(firstName)) {
			return false;
		}
		return true;
	}

	public static boolean validateActorSecondName(String secondName) {
		if (secondName == null) {
			return false;
		}
		return true;
	}

	public static boolean validateActorAge(int age) {
		if (age <= 0) {
			return false;
		}
		return true;
	}
	
	public static boolean validateActorID(int actorID) {
		if (actorID <= 0) {
			return false;
		}
		return true;
	}

}
