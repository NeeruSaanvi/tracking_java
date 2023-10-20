import React, { useEffect } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import Alert from "../../common/Alert";
import Spinner from "../../common/Spinner";
import Grid from "../../common/Grid/Grid";

const Ren1 = rec => {
  return <a href="#"> {rec.totalType} </a>;
};

const TestGrid = props => {
  //const {  } = props;
  const config = {
    id: "testGrid",
    url: "/dashboard/socialActivityTotals",
    params: null,
    columns: [
      {
        headerTitle: "Total Type",
        id: "totalType",
        type: "renderer",
        data: "totalType",
        renderer: Ren1
      },
      {
        headerTitle: "Total Members",
        id: "totalMembers",
        type: "text",
        data: "totalMembers"
      },
      {
        headerTitle: "Total Posts",
        id: "totalPosts",
        type: "text",
        data: "totalPosts"
      },
      {
        headerTitle: "Total Interactions",
        id: "totalInteractions",
        type: "text",
        data: "totalInteractions"
      }
    ],
    itemsPerPageOptions: [10, 25, 50, 100, 500]
  };

  return (
    <div className="item col-xs-12 col-md-8">
      <div className="panel panel-default">
        <div className="panel-body">
          <Grid config={config} />
        </div>
      </div>
    </div>
  );
};

export default TestGrid;
