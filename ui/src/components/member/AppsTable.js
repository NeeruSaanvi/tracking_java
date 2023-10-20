import React, { Component } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";
import sematable, {
  SortableHeader,
  SelectAllHeader,
  SelectRow
} from "sematable";

const columns = [
  {
    key: "id",
    header: "ID",
    searchable: true,
    sortable: true,
    primaryKey: true
  },
  { key: "name", header: "Name", searchable: true, sortable: true }
];

const propTypes = {
  headers: PropTypes.object.isRequired,
  mdata: PropTypes.array.isRequired
};

class AppsTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mdata: []
    };
  }

  componentDidMount() {
    fetch(process.env.API_URL + "/users")
      .then(response => response.json())
      .then(data => this.setState({ data }));
  }

  render() {
    const {
      headers: { select, id, name },
      mdata
    } = this.props;

    return (
      <div className="table-responsive">
        <table className="table table-sm table-striped table-hover">
          <thead>
            <tr>
              <SelectAllHeader {...select} />
              <SortableHeader {...id} />
              <SortableHeader {...name} />
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {mdata.map(app => (
              <tr
                key={app.id}
                className={`${select.isSelected(app) ? "table-info" : ""}`}
              >
                <td>
                  <SelectRow row={app} {...select} />
                </td>
                <td>{app.id}</td>
                <td>{app.name}</td>
                <td>
                  <Link to={`/settings/${app.id}`}>Settings</Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

AppsTable.propTypes = propTypes;
export default sematable("allApps", AppsTable, columns);
