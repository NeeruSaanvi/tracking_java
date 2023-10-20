import AuthService from "../../api/AuthService";
import {
  beginApiCall,
  apiCallError
} from "../../redux/actions/apiStatusActions";

export function loginSuccess(token) {
  return { type: "LOGIN_SUCCESS", token };
}

export function login(username, password) {
  const authService = new AuthService();
  return function(dispatch) {
    dispatch(beginApiCall());
    return authService
      .login(username, password)
      .then(res => {
        //dispatch(loginSuccess(res.token));
        console.log(res.token);
        window.location = "./dashboard.html";
      })
      .catch(error => {
        dispatch(apiCallError(error));
        throw error;
      });
  };
}
