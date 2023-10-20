import AuthService from "../api/AuthService";
import { handleResponse, handleError } from "../api/apiUtils";

const TOKEN_REFRESH_INTERVAL = parseInt(process.env.SESSION_TIMEOUT) - 60000;
const TOKEN_REFRESH_URL = process.env.API_URL + "/auth/refresh";
let timeout = null;

const refreshableFetch = (url, init) => {
  clearTimeout(timeout);

  timeout = setTimeout(
    () =>
      refreshableFetch(TOKEN_REFRESH_URL, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          token: new AuthService().getToken()
        })
      })
        .then(handleResponse)
        .then(res => {
          const token = res.token.replace("Bearer ", "");
          console.log("Refreshed token: " + token);
          new AuthService().setToken(token);
          return Promise.resolve(res);
        })
        .catch(handleError),
    TOKEN_REFRESH_INTERVAL
  );

  if (url === undefined || url === null) {
    url = TOKEN_REFRESH_URL;
    init = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        token: new AuthService().getToken()
      })
    };
  }

  return fetch(url, init);
};

export default refreshableFetch;
