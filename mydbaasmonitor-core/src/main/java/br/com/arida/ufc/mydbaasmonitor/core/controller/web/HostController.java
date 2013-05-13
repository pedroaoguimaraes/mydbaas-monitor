package main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.web;

import static main.java.br.com.arida.ufc.mydbaasmonitor.core.util.Utils.i18n;

import java.util.Date;
import java.util.List;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import main.java.br.com.arida.ufc.mydbaasmonitor.common.entity.resource.Host;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.web.common.AbstractController;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.controller.web.common.GenericController;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.DBaaSRepository;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.repository.HostRepository;
import main.java.br.com.arida.ufc.mydbaasmonitor.core.util.DataUtil;

/**
 * Class that manages the methods that the front-end hosts accesses.
 * @author David Araújo - @araujodavid
 * @version 2.0
 * @since May 13, 2013
 * Front-end: web/WEB-INF/jsp/host
 */

@Resource
public class HostController extends AbstractController implements GenericController<Host> {

	private HostRepository hostRepository;
	private DBaaSRepository dBaaSRepository;
	
	public HostController(Result result, Validator validator, HostRepository hostRepository, DBaaSRepository dBaaSRepository) {
		super(result, validator);
		this.hostRepository = hostRepository;
		this.dBaaSRepository = dBaaSRepository;
	}

	@Path("/hosts")
	@Override
	public void redirect() {
		this.result.redirectTo(this).list();		
	}

	@Path("/hosts/list")
	@Override
	public List<Host> list() {
		return hostRepository.all();
	}

	@Path("/hosts/new")
	@Override
	public void form() {
		//Includes the current date
		//List available DBaaS
		this.result
		.include("current_date", DataUtil.converteDateParaString(new Date()))
		.include("availableDBaaS", dBaaSRepository.all());
	}

	@Path("/hosts/add")
	public void add(final Host host, final String confirmPassword) {
		//Validations by vRaptor
		validator.checking(new Validations() {{
			that(!(host.getEnvironment().getId() == 0), "Environment", "host.environment.empty");
			that(!(host.getAlias() == null), "Alias", "host.alias.empty");
	        that(!(host.getAddress() == null), "Address", "host.address.empty");
	        that(!(host.getUser() == null), "Username", "host.username.empty");
	        that(!(host.getPort() == null), "Port", "host.port.empty");
	        if (host.getPassword() != null || confirmPassword != null) {
	        	that(host.getPassword().equals(confirmPassword), "Password", "host.password.not.checked");
			}	        
	    } });
		//If some validation is triggered are sent error messages to page
		validator.onErrorForwardTo(HostController.class).form();
		
		hostRepository.save(host);
		result
		.include("notice", i18n("host.save.ok"))
		.redirectTo(this).list();		
	}

	@Path("/hosts/edit/{entity.id}")
	@Override
	public Host edit(Host entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Path("/hosts/update")
	@Override
	public void update(Host entity) {
		// TODO Auto-generated method stub		
	}

	@Path("/hosts/view/{entity.id}")
	@Override
	public Host view(Host entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */

	@Override
	public void delete(Host entity) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void add(Host entity) {
		// TODO Auto-generated method stub		
	}

}
