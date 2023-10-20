import React, { useEffect } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { loadMemberCount } from "./memberCountActions";
import Alert from "../../common/Alert";
import Spinner from "../../common/Spinner";

const MemberCount = props => {
  const { loading, error, loadMemberCount, dashboardMemberCount } = props;
  const totalMembers =
    dashboardMemberCount.activeMembers + dashboardMemberCount.inactiveMembers;
  const activePerct = (dashboardMemberCount.activeMembers * 100) / totalMembers;
  const inactivePerct =
    (dashboardMemberCount.inactiveMembers * 100) / totalMembers;

  useEffect(() => {
    loadMemberCount();
  }, []);

  if (loading) {
    return <Spinner />;
  }

  if (error !== null) {
    return (
      <Alert
        show={error !== null}
        type={error !== null ? "failure" : "success"}
      >
        There was an error fetching data
      </Alert>
    );
  }

  return (
    <div className="item col-md-4 col-sm-6 col-xs-12">
      <div className="panel panel-default">
        <div className="panel-body">
          <h3>Member Count</h3>
          <div className="row">
            <div className="col-xs-6">
              <h4 className="text-headline margin-none">
                {" "}
                {dashboardMemberCount.activeMembers}
              </h4>
              <p className="text-light">
                <i className="fa fa-circle-o text-success fa-fw" /> Active
              </p>
            </div>
            <div className="col-xs-6">
              <h4 className="text-headline margin-none">
                {dashboardMemberCount.inactiveMembers}
              </h4>
              <p className="text-light">
                <i className="fa fa-circle-o text-danger fa-fw" /> Inactive
              </p>
            </div>
          </div>

          <div className="progress progress-mini">
            <div
              className="progress-bar progress-bar-success"
              style={{ width: `${activePerct}%` }}
            >
              <span className="sr-only">{activePerct}% Active Members</span>
            </div>
            <div
              className="progress-bar progress-bar-danger"
              style={{ width: `${inactivePerct}%` }}
            >
              <span className="sr-only">{inactivePerct}% Inactive Members</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

MemberCount.propTypes = {
  dashboardMemberCount: PropTypes.object.isRequired,
  loadMemberCount: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string
};

function mapStateToProps(state) {
  const loading = state.apiCallsInProgress > 0;
  const { error } = state;
  const { dashboardMemberCount } = state;
  return {
    loading,
    error,
    dashboardMemberCount
  };
}

const mapDispatchToProps = {
  loadMemberCount
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MemberCount);
