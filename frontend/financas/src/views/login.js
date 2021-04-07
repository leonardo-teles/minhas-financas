import React from 'react';

import Card from '../components/card';
import FormGroup from '../components/form-group';

class Login extends React.Component {

    state = {
        email: '',
        senha: ''
    }

    entrar = () => {
        console.log('email: ', this.state.email);
        console.log('senha: ', this.state.senha);
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6" style={ {position: 'relative', left: '300px'} }>
                        <div className="bs-docs-section">
                            <Card title="Login">
                                <div className="row">
                                    <div className="col-lg-12">
                                        <div className="bs-component">
                                            <fieldset>

                                                <FormGroup label="e-Mail: *" htmlFor="email">   
                                                    <input type="email" 
                                                           value={this.state.email}
                                                           onChange={e => this.setState({email: e.target.value})}
                                                           className="form-control" 
                                                           id="email" 
                                                           aria-describedby="email" 
                                                           placeholder="Digite o e-Mail"
                                                    />
                                                </FormGroup>

                                                <FormGroup label="Senha: *" htmlFor="senha">   
                                                    <input type="password" 
                                                           value={this.state.senha}  
                                                           onChange={e => this.setState({senha: e.target.value})}
                                                           className="form-control" 
                                                           id="senha" 
                                                           aria-describedby="senha" 
                                                           placeholder="Digite a senha"
                                                    />
                                                </FormGroup>
                                                <button onClick={this.entrar} className="btn btn-success">Entrar</button>
                                                <button className="btn btn-danger">Cadastrar</button>

                                            </fieldset>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Login;