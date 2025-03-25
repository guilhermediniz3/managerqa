import React from 'react';
import ProLayout from '@ant-design/pro-layout';
import { Link } from 'react-router-dom';

const BasicLayout = ({ children }) => {
  return (
    <ProLayout
      title="Meu Dashboard"
      logo="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"
      route={{
        path: '/',
        routes: [
          {
            path: '/dashboard',
            name: 'Dashboard',
            icon: 'dashboard',
          },
          {
            path: '/settings',
            name: 'Configurações',
            icon: 'setting',
          },
        ],
      }}
      menuItemRender={(item, dom) => <Link to={item.path}>{dom}</Link>}
    >
      {children}
    </ProLayout>
  );
};

export default BasicLayout;