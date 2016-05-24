package TablonDeAnunciosTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junio2013.AnuncianteNoExisteException;
import junio2013.Anuncio;
import junio2013.IBaseDeDatosDeAnunciantes;
import junio2013.IBaseDeDatosDePagos;
import junio2013.TablonDeAnuncios;

import org.junit.Test;

public class TablonDeAnunciosTest {

	@Test
	public void comprobarQueHayUnAnuncionEnTablon() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();
		assertEquals(tablon.anunciosPublicados(), 1);
	}

	@Test
	public void PublicarAnuncioQueSeaLaEmpresaYVerSiElNumeroDeAnunciosAumenta() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();
		Anuncio anuncio = new Anuncio("titulo", "texto", "LA EMPRESA");

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		assertEquals(tablon.anunciosPublicados(), 2);

	}

	@Test
	public void PublicarAnuncioQueNoSeaLaEmpresaYNoTieneFondosNoLoInserta() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		when(BDAnunciantes.buscarAnunciante("PEPE")).thenReturn(true);

		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		when(BDPagos.anuncianteTieneSaldo("PEPE")).thenReturn(false);

		Anuncio anuncio = new Anuncio("titulo", "texto", "PEPE");

		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		verify(BDAnunciantes).buscarAnunciante("PEPE");
		verify(BDPagos).anuncianteTieneSaldo("PEPE");
		verify(BDPagos, never()).anuncioPublicado("PEPE");
	}

	@Test
	public void PublicarAnuncioQueNoSeaLaEmpresaYSiTieneFondos() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		when(BDAnunciantes.buscarAnunciante("JUAN")).thenReturn(true);

		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		when(BDPagos.anuncianteTieneSaldo("JUAN")).thenReturn(true);

		Anuncio anuncio = new Anuncio("titulo", "texto", "JUAN");

		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		verify(BDAnunciantes).buscarAnunciante("JUAN");
		verify(BDPagos).anuncianteTieneSaldo("JUAN");
		verify(BDPagos).anuncioPublicado("JUAN");
	}

	@Test
	public void PublicarDosAnuncioDeEmpresaYBuscarElSegundoPorTituloYVerQueNoAumentaElTamaño() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();
		Anuncio anuncio = new Anuncio("titulo", "texto", "LA EMPRESA");
		Anuncio anuncio1 = new Anuncio("titulo1", "texto1", "LA EMPRESA");

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		tablon.publicarAnuncio(anuncio1, BDAnunciantes, BDPagos);

		assertEquals(tablon.buscarAnuncioPorTitulo("titulo1"), anuncio1);
		assertEquals(tablon.anunciosPublicados(), 3);

	}

	@Test
	public void PublicarDosAnuncioDeEmpresaYBorrarElPrimeroYCoprobarQueNoEsta() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();
		Anuncio anuncio = new Anuncio("titulo", "texto", "LA EMPRESA");
		Anuncio anuncio1 = new Anuncio("titulo1", "texto1", "LA EMPRESA");

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		tablon.publicarAnuncio(anuncio1, BDAnunciantes, BDPagos);
		tablon.borrarAnuncio("titulo", "LA EMPRESA");

		assertEquals(tablon.buscarAnuncioPorTitulo("titulo"), null);

	}

	@Test
	public void PublicarDosAnuncioIgualesMismoTituloYMismoAnunciante() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		when(BDAnunciantes.buscarAnunciante("JUAN")).thenReturn(true);

		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		when(BDPagos.anuncianteTieneSaldo("JUAN")).thenReturn(true);

		Anuncio anuncio = new Anuncio("titulo", "texto", "JUAN");
		Anuncio anuncio1 = new Anuncio("titulo", "texto1", "JUAN");

		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);
		tablon.publicarAnuncio(anuncio1, BDAnunciantes, BDPagos);

		assertEquals(tablon.anunciosPublicados(), 2);

	}

	@Test(expected = AnuncianteNoExisteException.class)
	public void SiSeIntentaPublicarUnAnuncioYNoExisteEnuncianteElevaExcepcion() {
		TablonDeAnuncios tablon = new TablonDeAnuncios();

		IBaseDeDatosDeAnunciantes BDAnunciantes = mock(IBaseDeDatosDeAnunciantes.class);
		when(BDAnunciantes.buscarAnunciante("JUAN")).thenReturn(false);

		IBaseDeDatosDePagos BDPagos = mock(IBaseDeDatosDePagos.class);
		when(BDPagos.anuncianteTieneSaldo("JUAN")).thenReturn(false);

		Anuncio anuncio = new Anuncio("titulo", "texto", "JUAN");

		tablon.publicarAnuncio(anuncio, BDAnunciantes, BDPagos);

	}

}
