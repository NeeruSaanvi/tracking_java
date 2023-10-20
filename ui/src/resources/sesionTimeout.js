import AuthService from "../api/AuthService";

export default function onClickForTimeout() {
  setTimeout(() => {
    console.log("Logging out the user..");
    new AuthService().logout();
  }, parseInt(process.env.SESSION_TIMEOUT));
}
