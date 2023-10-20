import React from "react";
import { render } from "react-dom";
import { BrowserRouter as Router } from "react-router-dom";
import configureStore from "../../redux/configureStore";
import { Provider as ReduxProvider } from "react-redux";
//import MemberApp from "./MemberApp";
//import GridReact from "./GridReact";
//import CustomPaginationActionsTable from "./CustomPaginationActionsTable";
import CustomPaginationMaterial from "./CustomPaginationMaterial";

const store = configureStore();

render(
  <ReduxProvider store={store}>
    <Router>
      <CustomPaginationMaterial />
    </Router>
  </ReduxProvider>,
  document.getElementById("app")
);
