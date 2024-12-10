var suspensionInterceptor = axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 451) {
            alert(`Account suspended!`);
            redirectLogout();
        }

        return Promise.reject(error);

    });
    
window.onload = redirectHome;


function redirectLogin() {
	redirectUrlPath(`/login`);
}

function redirectRegister() {
	redirectUrlPath(`/userRegistration`);
}

function redirectAbout() {
	redirectUrlPath(`/aboutUs`);
}

function redirectHome() {
	redirectUrlPathWithScript(`/home`);
}

function redirectAllBooks() {
	redirectUrlPathWithScript(`/books/allBooks`);
}

function redirectAllBooksById() {
	redirectUrlPathWithScript(`/books/allBooksById`);
}

function redirectAllBooksByRating() {
	redirectUrlPathWithScript(`/books/allBooksByRating`);
}

function redirectAllBooksByPrice() {
	redirectUrlPathWithScript(`/books/allBooksByPrice`);
}

function redirectBookForm() {
	redirectUrlPath(`/admin/createBook`);
}

function redirectAllGenres() {
	redirectUrlPathWithScript(`/admin/allGenres`);
}

function redirectGenreForm() {
	redirectUrlPath(`/admin/createGenre`);
}

function redirectUpdateGenre(genreId) {
	redirectUrlPath(`/admin/updateGenre/${genreId}`);
}

function redirectUpdateBook(bookId) {
	redirectUrlPath(`/admin/updateBook/${bookId}`);
}

function viewBookDetails(bookId) {
	redirectUrlPathWithScript(`/books/bookDetails/${bookId}`);
}

function redirectAddReview(bookId) {
	redirectUrlPath(`/reviews/createReview/${bookId}`);
}

function redirectAllReviews() {
	redirectUrlPathWithScript(`/admin/allReviews`);
}

function redirectAllRequestedReviews() {
	redirectUrlPathWithScript(`/admin/allRequestedReviews`);
}

function redirectPendingReview(reviewId) {
	redirectUrlPath(`/admin/pendingReviewDetails/${reviewId}`);
}

function redirectReviewDetails(reviewId) {
	redirectUrlPath(`/admin/reviewDetails/${reviewId}`);
}

function redirectWishlist() {
	redirectUrlPathWithScript(`/books/getWishList`);
}


function redirectLoyaltyCardInfo() {
	redirectUrlPath(`/loyaltyCards/cardInfo`);
}




function redirectCardRequestSent() {
	redirectUrlPath(`/loyaltyCards/cardRequestSent`);
}

function redirectAllCards() {
	redirectUrlPathWithScript(`/admin/getAllCards`);
}

function redirectAllCardRequests() {
	redirectUrlPathWithScript(`/admin/checkCardRequests`);
}

function addToCart(bookId) {
	redirectUrlPath(`/books/addToCart/${bookId}`);
}

function redirectItemAdded() {
	redirectUrlPath(`/cart/addItemCompleted`);
}

function redirectCart() {
	redirectUrlPath(`/cart/getCart`);
}

function redirectProcessOrder() {
	redirectUrlPath(`/orders/processOrder`);
}

function redirectAllOrders() {
	redirectUrlPathWithScript(`/admin/allOrders`);
}

function redirectAllCustomerOrders(customerId) {
	redirectUrlPathWithScript(`/admin/allOrders/${customerId}`);
}

function redirectOrderDetails(orderId) {
	redirectUrlPath(`/admin/getOrder/${orderId}`);
}

function redirectAllCustomers() {
	redirectUrlPathWithScript(`/admin/allCustomers`);
}

function redirectCustomerDetails(customerId) {
	redirectUrlPath(`/admin/getCustomer/${customerId}`);
}

function redirectAccountDetails() {
	redirectUrlPath(`/accountInfo`);
}

function redirectUpdateAddress(addressId) {
	redirectUrlPath(`/addresses/updateAddress/${addressId}`);
}

function redirectImageForm(bookId) {
	redirectUrlPathWithScript(`/admin/addImage/${bookId}`);
}



function redirectLogout() {
	axios.post(`http://localhost:8080/loggedout`)
		.then(() => {
			window.location.href = `/`;
		})

		.catch(() => {
			alert(`Logout error!`);
		})
};



function confirmLoginPass() {
	axios.post(`http://localhost:8080/loginPassConfirm`)
		.then(() => {
			window.location.href = `/`;
		})

		.catch(() => {
			redirectUrlPath(`/loginErrorPage`);
		})
};

function deleteGenre(genreId) {
	if (confirm(`Are you sure you want to delete this genre?\nIt will affect all related books!`)) {
		axios.get(`http://localhost:8080/admin/deleteGenre/${genreId}`)
			.then(() => {
				redirectAllGenres();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function deleteReview(reviewId) {
	if (confirm("Are you sure you want to reject this review?")) {
		axios.get(`http://localhost:8080/admin/rejectReview/${reviewId}`)
			.then(() => {
				redirectAllRequestedReviews();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};


function deleteBook(bookId) {
	if (confirm("Are you sure you want to delete this book?")) {
		axios.get(`http://localhost:8080/admin/deleteBook/${bookId}`)
			.then(() => {
				redirectAllBooks();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};



function authorizeReview(reviewId) {
	if (confirm(`Are you sure you want to authorize this review?`)) {
		axios.post(`http://localhost:8080/admin/authorizeReview/${reviewId}`)
			.then(() => {
				redirectAllReviews();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function addToWishlist(bookId) {
	axios.post(`http://localhost:8080/books/addToWishList/${bookId}`)
		.then(() => {
			alert(`Book added to wish list!`);
		})

		.catch(() => {
			alert(`Failed!`);
		})
};


function removeFromWishlist(bookId) {
	if (confirm(`Remove this book from wish list?`)) {
		axios.get(`http://localhost:8080/books/deleteFromWishList/${bookId}`)
			.then(() => {
				redirectWishlist();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};



function getLoyaltyCard() {
	axios.post(`http://localhost:8080/loyaltyCards/createCardRequest`)
		.then(() => {
			redirectCardRequestSent();
		})

		.catch(() => {
			alert(`Failed!`);
		})
};



function authorizeCard(requestId) {
	if (confirm(`Authorize loyalty card for this customer?`)) {
		axios.get(`http://localhost:8080/admin/authorizeCard/${requestId}`)
			.then(() => {
				redirectAllCardRequests();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function denyCard(requestId) {
	if (confirm(`Deny loyalty card for this customer?`)) {
		axios.get(`http://localhost:8080/admin/rejectCard/${requestId}`)
			.then(() => {
				redirectAllCardRequests();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};

function eraseItem(itemId) {
	if (confirm(`Remove this item from cart?`)) {
		axios.get(`http://localhost:8080/cart/deleteItem/${itemId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function eraseAllItems(cartId) {
	if (confirm(`Are you sure you want to clear your cart?`)) {
		axios.get(`http://localhost:8080/cart/deleteAllItems/${cartId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function deleteOrder(orderId) {
	if (confirm(`Are you sure you want to clear this order?`)) {
		axios.get(`http://localhost:8080/admin/deleteOrder/${orderId}`)
			.then(() => {
				redirectAllOrders();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};

function suspendUser(userId) {
	if (confirm(`Are you sure you want to suspend this user?`)) {
		axios.get(`http://localhost:8080/admin/suspendUser/${userId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function reactivateUser(userId) {
	if (confirm(`Are you sure you want to reactivate this user?`)) {
		axios.get(`http://localhost:8080/admin/reactivateUser/${userId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};



function redirectOrderConfirmation(cartId) {
	axios.get(`http://localhost:8080/orders/confirmOrder/${cartId}`)
		.then(() => {
			redirectUrlPath(`orders/confirmOrder/${cartId}`);
		})

		.catch((error) => {
			if (error.response.status === 422) {
				redirectUrlPath(`/cart/invalidCart`);
			} else {
				alert(`Failed!`);
			}

		})
};




function deleteCustomer(customerId) {
	if (confirm(`Are you sure you want to remove this customer?`)) {
		axios.get(`http://localhost:8080/admin/deleteCustomer/${customerId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};


function login() {
	var formData = new FormData(document.getElementById("loginForm"));
	var serializedFormData = new URLSearchParams(formData).toString();

	document.cookie = "JSESSIONID=" + Math.random() + "; SameSite=None; Secure";

	axios.post(`http://localhost:8080/login`, serializedFormData, {
		headers: {
			'Content-Type': `application/x-www-form-urlencoded`
		},
		withCredentials: true
	})
		.then(() => {
			confirmLoginPass();
		})
		.catch(() => {
			alert(`Failed!`);
		});
};

function storeBook() {
	if (validateBook()) {
		var formData = new FormData(document.getElementById("bookForm"));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/admin/createBook`, serializedFormData)
			.then(() => {
				redirectAllBooks();
			})

			.catch(() => {
				alert(`Failed!`);
			})

	}
}

function addGenre() {
	if (validateGenre()) {
		var formData = new FormData(document.getElementById(`genreForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/admin/createGenre`, serializedFormData)
			.then(() => {
				redirectAllGenres();
			})

			.catch((error) => {
				if (error.response.status === 409) {
					alert(error.response.data);
				} else {
					alert(`Failed!`);
				}

			})

	}
}

function updateGenre(genreId) {
	if (validateGenre()) {
		var formData = new FormData(document.getElementById(`genreForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/admin/updateGenre/${genreId}`, serializedFormData)
			.then(() => {
				redirectAllGenres();
			})

			.catch((error) => {
				if (error.response.status === 409) {
					alert(error.response.data);
				} else {
					alert(`Failed!`);
				}

			})

	}
}

function sendImage(bookId) {
	var fileInput = document.getElementById(`file`);

	if (fileInput.files.length === 0) {
		alert(`Please select an image to upload.`);
		return;
	}

	var formData = new FormData(document.getElementById(`imageForm`));

	axios.post(`http://localhost:8080/admin/storeImage/${bookId}`, formData)
		.then(() => {
			redirectAllBooks();
		})
		.catch((error) => {
			if (error.response.status === 406) {
				alert(error.response.data);
			} else {
				alert(`Failed!`);
			}

		});
}

function confirmRegistration() {
	var formData = new FormData(document.getElementById(`registrationForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateRegForm() && ValidatePassword()) {
		axios.post(`http://localhost:8080/userRegistration`, serializedFormData)
			.then(() => {
				redirectUrlPath(`/registerComplete`);
			})

			.catch((error) => {
				if (error.response.status === 409) {
					redirectUrlPath(`/registerFail`);
				} else {
					alert(`Failed!`);
				}

			})

	}
}

function addItemToCart() {
	var formData = new FormData(document.getElementById(`itemForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateItem()) {
		axios.post(`http://localhost:8080/cart/addToCart`, serializedFormData)
			.then(() => {
				redirectItemAdded();
			})

			.catch((error) => {
			    if(error.response.status === 422){
			        alert(error.response.data);
			    }else{
			        alert(`Failed`);
			    }
			})
	}
}

function updateAddress() {
	var formData = new FormData(document.getElementById(`addressForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateDeliveryAddress()) {
		axios.post(`http://localhost:8080/addresses/createAddress`, serializedFormData)
			.then(() => {
				redirectAccountDetails();
			})

			.catch(() => {
				alert(`Failed`);
			})
	}
}

function addReview() {
	var formData = new FormData(document.getElementById(`reviewForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateReview()) {
		axios.post(`http://localhost:8080/reviews/createReview`, serializedFormData)
			.then(() => {
				redirectUrlPath(`/reviews/reviewSentCompleted`);
			})

			.catch((error) => {
				if (error.response.status === 409) {
					alert(error.response.data);
				} else {
					alert(`Failed!`);
				}
			})
	}
}

function searchBook() {
	if (validateKeyword()) {
		var keyword = document.getElementById(`keyword`).value;
		redirectUrlPathWithScript(`/books/searchBooks/${keyword}`);
	}
}

function redirectUrlPath(path) {
	axios.get(path)
		.then(response => {
			document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
		})
		.catch(error => {
			console.error(`Error loading home page:`, error);
		});
};

function redirectUrlPathWithScript(path) {
	axios.get(path)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
};





