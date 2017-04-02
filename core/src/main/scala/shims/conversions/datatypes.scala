package shims.conversions

import scalaz.{~>, \/}
import cats.arrow.FunctionK

trait AsScalaz[-I, +O] {
  def c2s(i: I): O
}

trait AsCats[-I, +O] {
  def s2c(i: I): O
}

trait EitherConversions {

  implicit def eitherAs[A, B] = new AsScalaz[Either[A, B], A \/ B] with AsCats[A \/ B, Either[A, B]] {
    def c2s(e: Either[A, B]) = \/.fromEither(e)
    def s2c(e: A \/ B) = e.fold(l => Left(l), r => Right(r))
  }
}

trait FunctionKConversions {

  implicit def functionkAs[F[_], G[_]] = new AsScalaz[FunctionK[F, G], F ~> G] with AsCats[F ~> G, FunctionK[F, G]] {
    def c2s(f: FunctionK[F, G]) = λ[F ~> G](f(_))
    def s2c(f: F ~> G) = λ[FunctionK[F, G]](f(_))
  }
}

/*trait FreeConversions extends MonadConversions {

  implicit def freeAs[S[_], A] = new AsScalaz[cats.free.Free[S, A], scalaz.Free[S, A]] with AsCats[scalaz.Free[S, A], cats.free.Free[S, A]] {

    def c2s(f: cats.free.Free[S, A]) =
      f.foldMap[scalaz.Free[S, ?]](λ[FunctionK[S, scalaz.Free[S, ?]]](scalaz.Free.liftF(_)))

    def s2c(f: scalaz.Free[S, A]) =
      f.foldMap[cats.free.Free[S, ?]](λ[S ~> cats.free.Free[S, ?]](cats.free.Free.liftF(_)))
  }
}*/