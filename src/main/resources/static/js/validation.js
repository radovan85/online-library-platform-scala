function validateBook() {

	var name = document.getElementById("name").value.trim();
	var publisher = document.getElementById("publisher").value.trim();
	var author = document.getElementById("author").value.trim();
	var description = document.getElementById("description").value.trim();
	var language = document.getElementById("language").value.trim();
	var publishedYear = document.getElementById("publishedYear").value;
	var pageNumber = document.getElementById("pageNumber").value;
	var price = document.getElementById("price").value;
	var cover = document.getElementById("cover").value;
	var letter = document.getElementById("letter").value;
	var genreId = document.getElementById("genreId").value;

	var nameError = document.getElementById("nameError");
	var publisherError = document.getElementById("publisherError");
	var authorError = document.getElementById("authorError");
	var descriptionError = document.getElementById("descriptionError");
	var languageError = document.getElementById("languageError");
	var publishedYearError = document.getElementById("publishedYearError");
	var pageNumberError = document.getElementById("pageNumberError");
	var priceError = document.getElementById("priceError");
	var coverError = document.getElementById("coverError");
	var letterError = document.getElementById("letterError");
	var genreIdError = document.getElementById("genreIdError");

	var returnValue = true;
	var publishedYearNum = Number(publishedYear);
	var pageNumberNum = Number(pageNumber);
	var priceNum = Number(price);

	if (name === "" || name.length > 50) {
		nameError.style.visibility = "visible";
		returnValue = false;
	} else {
		nameError.style.visibility = "hidden";
	}

	if (publisher === "" || publisher.length > 40) {
		publisherError.style.visibility = "visible";
		returnValue = false;
	} else {
		publisherError.style.visibility = "hidden";
	}

	if (author === "" || author.length > 40) {
		authorError.style.visibility = "visible";
		returnValue = false;
	} else {
		authorError.style.visibility = "hidden";
	}

	if (description === "" || description.length > 90) {
		descriptionError.style.visibility = "visible";
		returnValue = false;
	} else {
		descriptionError.style.visibility = "hidden";
	}

	if (language === "" || language.length > 30) {
		languageError.style.visibility = "visible";
		returnValue = false;
	} else {
		languageError.style.visibility = "hidden";
	}

	var d = new Date();
	var currentYear = d.getFullYear();
	if (publishedYear === "" || publishedYearNum > currentYear) {
		publishedYearError.style.visibility = "visible";
		returnValue = false;
	} else {
		publishedYearError.style.visibility = "hidden";
	}

	if (pageNumber === "" || pageNumberNum < 10) {
		pageNumberError.style.visibility = "visible";
		returnValue = false;
	} else {
		pageNumberError.style.visibility = "hidden";
	}

	if (price === "" || priceNum < 5) {
		priceError.style.visibility = "visible";
		returnValue = false;
	} else {
		priceError.style.visibility = "hidden";
	}

	if (cover === "") {
		coverError.style.visibility = "visible";
		returnValue = false;
	} else {
		coverError.style.visibility = "hidden";
	}

	if (letter === "") {
		letterError.style.visibility = "visible";
		returnValue = false;
	} else {
		letterError.style.visibility = "hidden";
	}
	;

	if (genreId === "") {
		genreIdError.style.visibility = "visible";
		returnValue = false;
	} else {
		genreIdError.style.visibility = "hidden";
	}

	return returnValue;

};

function validateDeliveryAddress() {

	var address = document.getElementById("address").value.trim();
	var city = document.getElementById("city").value.trim();
	var state = document.getElementById("state").value.trim();
	var postcode = document.getElementById("postcode").value.trim();
	var country = document.getElementById("country").value.trim();

	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var stateError = document.getElementById("stateError");
	var postcodeError = document.getElementById("postcodeError");
	var countryError = document.getElementById("countryError");

	var returnValue = true;

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	}

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	}

	if (state === "" || state.length > 40) {
		stateError.style.visibility = "visible";
		returnValue = false;
	} else {
		stateError.style.visibility = "hidden";
	}

	if (postcode.length < 5 || postcode.length > 10) {
		postcodeError.style.visibility = "visible";
		returnValue = false;
	} else {
		postcodeError.style.visibility = "hidden";
	}

	if (country === "" || country.length > 40) {
		countryError.style.visibility = "visible";
		returnValue = false;
	} else {
		countryError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateGenre() {

	var name = document.getElementById("name").value.trim();
	var description = document.getElementById("description").value.trim();

	var nameError = document.getElementById("nameError");
	var descriptionError = document.getElementById("descriptionError");

	var returnValue = true;

	if (name === "" || name.length > 30) {
		nameError.style.visibility = "visible";
		returnValue = false;
	} else {
		nameError.style.visibility = "hidden";
	}

	if (description === "" || description.length > 90) {
		descriptionError.style.visibility = "visible";
		returnValue = false;
	} else {
		descriptionError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateRegForm() {

	var firstName = document.getElementById("firstName").value.trim();
	var lastName = document.getElementById("lastName").value.trim();
	var email = document.getElementById("email").value.trim();
	var password = document.getElementById("password").value.trim();
	var dateOfBirthStr = document.getElementById("dateOfBirthStr").value;
	var address = document.getElementById("address").value.trim();
	var city = document.getElementById("city").value.trim();
	var state = document.getElementById("state").value.trim();
	var postcode = document.getElementById("postcode").value.trim();
	var country = document.getElementById("country").value.trim();

	var firstNameError = document.getElementById("firstNameError");
	var lastNameError = document.getElementById("lastNameError");
	var emailError = document.getElementById("emailError");
	var passwordError = document.getElementById("passwordError");
	var dateOfBirthStrError = document.getElementById("dateOfBirthStrError");
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var stateError = document.getElementById("stateError");
	var postcodeError = document.getElementById("postcodeError");
	var countryError = document.getElementById("countryError");

	var dateOfBirth = new Date(dateOfBirthStr);

	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var date_regex = /^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/g;

	var returnValue = true;

	if (firstName === "" || firstName.length > 30) {
		firstNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		firstNameError.style.visibility = "hidden";
	}

	if (lastName === "" || lastName.length > 30) {
		lastNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		lastNameError.style.visibility = "hidden";
	}

	if (email === "" || email.length > 50 || !regEmail.test(email)) {
		emailError.style.visibility = "visible";
		returnValue = false;
	} else {
		emailError.style.visibility = "hidden";
	}

	if (password.length < 6 || password.length > 30) {
		passwordError.style.visibility = "visible";
		returnValue = false;
	} else {
		passwordError.style.visibility = "hidden";
	}

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	}

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	}

	if (state === "" || state.length > 40) {
		stateError.style.visibility = "visible";
		returnValue = false;
	} else {
		stateError.style.visibility = "hidden";
	}

	if (postcode.length < 5 || postcode.length > 10) {
		postcodeError.style.visibility = "visible";
		returnValue = false;
	} else {
		postcodeError.style.visibility = "hidden";
	}

	if (country === "" || country.length > 40) {
		countryError.style.visibility = "visible";
		returnValue = false;
	} else {
		countryError.style.visibility = "hidden";
	}

	var today = new Date();
	var validMinDate = new Date(today.getFullYear() - 18, today.getMonth(),
		today.getDate(), today.getHours(), today.getMinutes());

	if (dateOfBirth > validMinDate) {
		dateOfBirthStrError.style.visibility = "visible";
		returnValue = false
	} else {
		dateOfBirthStrError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateReview() {

	var text = document.getElementById("text").value.trim();
	var rating = document.getElementById("rating").value;

	var textError = document.getElementById("textError");
	var ratingError = document.getElementById("ratingError");

	var returnValue = true;

	if (text === "" || text.length > 255) {
		textError.style.visibility = "visible";
		returnValue = false;
	} else {
		textError.style.visibility = "hidden";
	}

	if (rating === "") {
		ratingError.style.visibility = "visible";
		returnValue = false;
	} else {
		ratingError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateKeyword() {
	var returnValue = true;
	var keyword = document.getElementById("keyword").value.trim();
	var keywordError = document.getElementById("keywordError");

	if (keyword === "") {
		keywordError.style.visibility = "visible";
		returnValue = false;
	} else {
		keywordError.style.visibility = "hidden";
	}

	return returnValue;
};

function ValidatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(event) {
	if (!/^[\d.]$/.test(event.key) && !['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(event.key)) {
		event.preventDefault();
	}
};

function validateItem() {
	var quantity = document.getElementById("quantity").value;
	var quantityError = document.getElementById("quantityError");
	var quantityNum = Number(quantity);
	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 50) {
		quantityError.style.visibility = "visible";
		returnValue = false;
	} else {
		quantityError.style.visibility = "hidden";
	}

	return returnValue;
}