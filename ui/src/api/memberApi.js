import { handleResponse, handleError, getHeaders } from "./apiUtils";
const baseUrl = process.env.API_URL;

export function getAllMembers() {
  return fetch(baseUrl + "/users", {
    method: "GET",
    headers: getHeaders()
  })
    .then(handleResponse)
    .catch(handleError);
}
