import React, { useEffect } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { loadMostUsedKeywords } from "./mostUsedKeywordsActions";

const MostUsedKeywords = props => {
  const {
    loading,
    error,
    loadMostUsedKeywords,
    dashboardMostUsedKeywords
  } = props;

  useEffect(() => {
    loadMostUsedKeywords();
  }, []);

  return (
    <div className="item col-md-4 col-sm-6 col-xs-12">
      <div className="panel panel-default">
        <div className="panel-heading">
          <h4 className="panel-title">Top 5 Keywords</h4>
        </div>
        <table className="table table-leaderboard margin-none">
          <tbody>
            {dashboardMostUsedKeywords.map((value, index) => {
              return (
                <tr key={index + 1}>
                  <td width="20">{index + 1}</td>
                  <td>
                    <a href="#">
                      <i className="fa fa-flag text-muted" /> {value.name}
                    </a>
                  </td>
                  <td>
                    <span className="pull-right">{value.socialType}</span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

MostUsedKeywords.propTypes = {
  dashboardMostUsedKeywords: PropTypes.array.isRequired,
  loadMostUsedKeywords: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string
};

function mapStateToProps(state) {
  const loading = state.apiCallsInProgress > 0;
  const { error } = state;
  const { dashboardMostUsedKeywords } = state;
  return {
    loading,
    error,
    dashboardMostUsedKeywords
  };
}

const mapDispatchToProps = {
  loadMostUsedKeywords
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MostUsedKeywords);
