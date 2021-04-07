import React from 'react';

import Login from './views/login';

import './custom.css';
import 'bootswatch/dist/flatly/bootstrap.min.css';
import CadastroUsuario from './views/cadastroUsuario';

class App extends React.Component {
  render(){
    return(
      <div>
          <CadastroUsuario />
      </div>
    )
  }
}

export default App;
