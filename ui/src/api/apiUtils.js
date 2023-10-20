import AuthService from "./AuthService";

export async function handleResponse(response) {
  if (response.ok) {
    const res = await response.json();

    if (res.list === undefined || res.list === true) {
      return res; //if returned type is list than return everything
    } else {
      return res.data; // this means returned type is not list but an object with wrapper class
    }
  }
  if (response.status === 401) {
    const authService = new AuthService();
    authService.logout();
    window.location = "./login.html";
  }
  if (response.status < 200 || response.status >= 300) {
    const error = await response.json();
    throw new Error(error.message);
  }
  throw new Error("Network response was not ok.");
}

// In a real app, would likely call an error logging service.
export function handleError(error) {
  // eslint-disable-next-line no-console
  console.error("API call failed. " + error);
  throw error;
}

export function getHeaders() {
  const authService = new AuthService();

  const headers = {
    Accept: "application/json",
    "Content-Type": "application/json"
  };

  if (authService.loggedIn()) {
    headers["Authorization"] = authService.getToken();
  }

  return headers;
}
