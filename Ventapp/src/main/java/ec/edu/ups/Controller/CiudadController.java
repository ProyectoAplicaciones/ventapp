package ec.edu.ups.Controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.Dao.CiudadDAO;
import ec.edu.ups.Model.Ciudad;

@ManagedBean
@RequestScoped
public class CiudadController{

	private Ciudad ciudad;
	private List<Ciudad> ciudades;
	private String id;
	
	@Inject
	private CiudadDAO cDAO;
	
	@Inject
    private FacesContext facesContext;
	
	@PostConstruct
	public void init() {
		ciudad = new Ciudad();
		loadCiudades();
	}
	
	public String loadDatosEditar(String nombre) {
		ciudad = cDAO.leer(nombre);
		return "crearciudad"; //Llama a la pagina para que cargue el objeto persona cargado
	}
	
	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public List<Ciudad> getCiudades() {
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		loadDatosEditar(id);
		this.id = id;
	}

	@Override
	public String toString() {
		return "CiudadController [ciudad=" + ciudad + ", ciudades=" + ciudades + ", id=" + id + ", cDAO=" + cDAO + "]";
	}
	
	public String eliminar(String nombre) {//ActionController
		System.out.println(toString());
		try{
			cDAO.borrar(nombre);
			loadCiudades();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Error al eliminar ciudad especificada.");
            facesContext.addMessage(null, m);
		}
		return "listadoCiudades";
	}
	
	public String guardar() {//ActionController
		System.out.println(toString());
		
		try{
			if (this.id!=null){
				cDAO.insertar(ciudad);
			}else{
				cDAO.actualizar(ciudad);
			}
			loadCiudades();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "No se pudo guardar.");
            facesContext.addMessage(null, m);
		}
		loadCiudades();
		return "listadoCiudades";
	}
	
	private void loadCiudades() {
		ciudades=cDAO.listadoCiudades();
	}
	
	private String getRootErrorMessage(Exception e) {
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            return errorMessage;
        }
        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }
	
}
