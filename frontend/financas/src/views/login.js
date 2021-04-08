import React from 'react';
import { withRouter } from 'react-router-dom';

import Card from '../components/card';
import FormGroup from '../components/form-group';

import UsuarioService from '../app/service/usuarioService';

class Login extends React.Component {

    state = {
        email: '',
        senha: '',
        mensagemErro: null
    }

    constructor() {
        super();
        this.service = new UsuarioService();
    }

    entrar = () => {
        this.service.autenticar({
                email: this.state.email,
                senha: this.state.senha
        })
        .then(response => {
                localStorage.setItem('_usuario_logado', JSON.stringify(response.data));
                this.props.history.push('/home');
        })
        .catch(error => {
                this.setState({mensagemErro: error.response.data})
        })
    }

    prepareCadastrar = () => {
        this.props.history.push('/cadastro-usuario');
    }

    render() {
        return (            
            <div className="row">
                <div className="col-md-6" style={ {position: 'relative', left: '300px'} }>
                    <div className="bs-docs-section">
                        <Card title="Login">
                            <div className="row">
                                <span>{this.state.mensagemErro}</span>
                            </div>
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
                                            <button onClick={this.prepareCadastrar} className="btn btn-danger">Cadastrar</button>

                                        </fieldset>
                                    </div>
                                </div>
                            </div>
                        </Card>
                    </div>
                </div>
            </div>            
        )
    }
}

export default withRouter(Login);