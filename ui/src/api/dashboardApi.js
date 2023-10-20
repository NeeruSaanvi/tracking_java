import { handleResponse, handleError, getHeaders } from "./apiUtils";
const baseUrl = process.env.API_URL;

export function getActiveInActiveMembers() {
  return fetch(baseUrl + "/dashboard/memberCounts", {
    method: "GET",
    headers: getHeaders()
  })
    .then(handleResponse)
    .catch(handleError);
}

export function getMostUsedKeywords() {
  return fetch(baseUrl + "/dashboard/mostUsedKeywords", {
    method: "GET",
    headers: getHeaders()
  })
    .then(handleResponse)
    .catch(handleError);
}

export function getSocialActivitytotals() {
  return fetch(baseUrl + "/dashboard/socialActivityTotals", {
    method: "GET",
    headers: getHeaders()
  })
    .then(handleResponse)
    .catch(handleError);
}
