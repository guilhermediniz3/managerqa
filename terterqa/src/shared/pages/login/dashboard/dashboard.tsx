import React from 'react';
import NavHorizontal from '../../../components/navs/horizontal/NavHorizontal';
import './styless.css';


const Dashboard: React.FC = () => {
  return (
    <div className="dashboardContainer">
      <NavHorizontal />
      <div className="dashboardContent">
        <h1>Bem-vindo ao Dashboard</h1>
        <p>Conteúdo principal da página.</p>
      </div>
    </div>
  );
};

export default Dashboard;