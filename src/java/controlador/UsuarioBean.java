/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import users.Usuarios;

import java.util.List;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author PC
 */
@ManagedBean
@ViewScoped
public class UsuarioBean {
    


private int id;
    private String email;
    private String name;
    private String lastname;
    private List<Usuarios> listaUsuarios;  // Una lista de usuarios
    private boolean editando = false; // Agrega esta propiedad

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    // Método para obtener la lista de usuarios
    // START https://www.youtube.com/watch?v=R2UjK9TcYMw
    private List<Usuarios> obtenerListaUsuarios() {
        List<Usuarios> lista = null;
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sesion.beginTransaction();
        String hql = "FROM Usuarios";
        try {
            lista = sesion.createQuery(hql).list();
            t.commit();
            sesion.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista; 
    }
    // END https://www.youtube.com/watch?v=R2UjK9TcYMw

    public UsuarioBean() {
        // Cargar la lista de usuarios al inicio
        listaUsuarios = obtenerListaUsuarios();
    }
    // Métodos para agregar, editar, eliminar y obtener usuarios

    public void agregarUsuario() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Usuarios usuario = new Usuarios(email, name, lastname);
            session.save(usuario);
            tx.commit();
            listaUsuarios = obtenerListaUsuarios(); // Actualiza la lista de usuarios
            reiniciarCampos(); // Reinicia los campos de entrada después de agregar
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    private void reiniciarCampos() {
        email = "";
        name = "";
        lastname = "";
        editando = false; 
    }

    public void editarUsuario(Usuarios usuario) {
        id = usuario.getId();
        email = usuario.getEmail();
        name = usuario.getName();
        lastname = usuario.getLastname();
        editando = true; // Establece el modo de edición a true
    }

    public void actualizarUsuario() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Usuarios usuario = (Usuarios) session.get(Usuarios.class, id);
            usuario.setEmail(email);
            usuario.setName(name);
            usuario.setLastname(lastname);
            session.update(usuario);
            tx.commit();
            listaUsuarios = obtenerListaUsuarios(); // Actualiza la lista de usuarios
            id = 0; // Reinicia el id después de actualizar
            reiniciarCampos(); 
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void eliminarUsuario(Usuarios usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(usuario);
            tx.commit();
            listaUsuarios = obtenerListaUsuarios(); // Actualiza la lista de usuarios
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}