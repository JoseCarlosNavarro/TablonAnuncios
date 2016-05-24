package junio2013;

/**
 * Created with IntelliJ IDEA.
 * User: anton_000
 * Date: 11/06/13
 * Time: 8:40
 * To change this template use File | Settings | File Templates.
 */
public interface IBaseDeDatosDePagos {
  //public void pagarAnuncios(String anunciante, int numeroDeAnuncios);

  public void anuncioPublicado(String anunciante);

  public boolean anuncianteTieneSaldo(String anunciante);
}
