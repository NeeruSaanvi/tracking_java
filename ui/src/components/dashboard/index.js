import React from "react";
import { render } from "react-dom";
import { BrowserRouter as Router } from "react-router-dom";
import configureStore from "../../redux/configureStore";
import { Provider as ReduxProvider } from "react-redux";
import DashboardApp from "./DashboardApp";

const store = configureStore();

render(
  <ReduxProvider store={store}>
    <Router>
      <DashboardApp />
    </Router>
  </ReduxProvider>,
  document.getElementById("app")
);
