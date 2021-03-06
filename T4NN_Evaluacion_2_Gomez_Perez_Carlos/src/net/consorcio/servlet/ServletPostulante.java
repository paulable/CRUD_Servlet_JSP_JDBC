package net.consorcio.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.consorcio.entidad.Postulante;
import net.consorcio.service.PostulanteService;


@WebServlet("/ServletPostulante")
public class ServletPostulante extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PostulanteService servicioPostulante;
	
    public ServletPostulante() {
        super();
        
        servicioPostulante= new PostulanteService();
        
        
        
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipo=request.getParameter("accion");
		if(tipo.equals("REGISTRAR"))
			registrar(request,response);
		else if(tipo.equals("ACTUALIZAR"))
			actualizar(request,response);
		else if(tipo.equals("ELIMINAR"))
			eliminar(request,response);
		else if(tipo.equals("BUSCAR"))
			buscar(request,response);
		else if(tipo.equals("LISTAR"))
			listar(request,response);
		
		
		
		
	}


	private void listar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Postulante> lista=servicioPostulante.listar();
		JsonArrayBuilder arreglo=Json.createArrayBuilder();
		for(Postulante bean: lista) {
			JsonObject obj=Json.createObjectBuilder().add("codigo", bean.getCodigo()).
													  add("nombre", bean.getNombre()).
													  add("apellido", bean.getApellido()).
													  add("dni", bean.getDniPostulante()).
													  add("hijos", bean.getNumHijos()).build();

			arreglo.add(obj);
		}
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter salida=response.getWriter();
		salida.println(arreglo.build());
	}


	private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Postulante bean;
		String cod;
		cod=request.getParameter("codigo");
		bean=servicioPostulante.buscar(Integer.parseInt(cod));
		request.setAttribute("postulante",bean);
		request.getRequestDispatcher("/actualizar.jsp").forward(request, response);
	}


	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cod;
		cod=request.getParameter("codigo");
		int salida = servicioPostulante.eliminar(Integer.parseInt(cod));

		if(salida != -1)
			request.setAttribute("MENSAJE", "Se eliminó correctamente");
		else
			request.setAttribute("MENSAJE", "Error al eliminar");
		
		request.getRequestDispatcher("/actualizar.jsp").forward(request, response);

	}


	private void actualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cod,nom, ape, dni, hijos;
		cod = request.getParameter("codigo");
		nom = request.getParameter("nombre");
		ape = request.getParameter("apellido");
		dni = request.getParameter("dni");
		hijos = request.getParameter("hijos");
		
		Postulante bean = new Postulante();
		
		bean.setCodigo(Integer.parseInt(cod));
		bean.setNombre(nom);
		bean.setApellido(ape);
		bean.setDniPostulante(Integer.parseInt(dni));
		bean.setNumHijos(Integer.parseInt(hijos));
		
		int salida = servicioPostulante.actualizar(bean);
		if(salida != -1)
			request.setAttribute("MENSAJE", "Se actualizo correctamente");
		else
			request.setAttribute("MENSAJE", "Error al actualizar");
		
		request.getRequestDispatcher("/actualizar.jsp").forward(request, response);

	}


	private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//PASO 1: variables para leer los valores de las cajas
				String nom, ape, dni, hijos;
				//entre comillas son los names
				nom = request.getParameter("nombre");
				ape = request.getParameter("apellido");
				dni = request.getParameter("dni");
				hijos = request.getParameter("hijos");
				//PASO 2: crear un objeto de la clase Postulante
				Postulante bean = new Postulante();
				//PASO 3: setear los atributos del objeto bean con los valores de las variables
				bean.setNombre(nom);
				bean.setApellido(ape);
				bean.setDniPostulante(Integer.parseInt(dni));
				bean.setNumHijos(Integer.parseInt(hijos));
				//PASO:4 invocar al metodo addPostulante
				int salida = servicioPostulante.registrar(bean);
				//PASO 5: validar el valor de salida y crear el atributo
				if(salida != -1)
					request.setAttribute("MENSAJE", "Registro guardado correctamente");
				else
					request.setAttribute("MENSAJE", "Error al guardar correctamente");
				
				request.getRequestDispatcher("/crear.jsp").forward(request, response);
		
	}

}
