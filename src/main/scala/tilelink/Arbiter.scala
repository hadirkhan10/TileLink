package tilelink
import chisel3._

class Arbiter(M: Int)(implicit val conf: TLConfiguration) extends Module {
  val io = IO(new Bundle {
    val req_i = Input(Vec(M, Bool())) // a_valid signals coming from all the hosts
    val data_i = Vec(M, Flipped(new TL_H2D))  // Channel A data coming from all the hosts
    val gnt_o = Output(Vec(M, Bool()))  // The grant output indicating which host was given the bus
    val valid_o = Output(Bool())  // The valid signal from the host to the device
    val data_o = new TL_H2D // The Channel A data of selected host forwarded to the device
    val ready_i = Input(Bool()) // The ready signal coming from the device
  })
  // by default setting the arbiter to pass the request of first host

  io.valid_o := io.req_i(0)
  io.data_o := io.data_i(0)

  for(i <- 0 until M) {
    when(i.asUInt === 0.U) {
      io.gnt_o(i) := io.valid_o && io.ready_i
    } .otherwise {
      io.gnt_o(i) := false.B
    }
  }


}