<h1 align="center"> Parallel and Distributed Calculations</h1>

<h3 align="center">Lab 1</h3>

<p align="center">Semaphores, mutexes, events, critical sections in WinAPI</p>

<b>Task:</b> 

1. Develop a parallel algorithm for solving a mathematical problem `MU = MD * MC * d + max (Z) * MR` using `WinAPI` library in `C++`;
2. Identify shared resources;
3. Describe the algorithm of each thread (T1 - Ti) with the definition of critical areas and synchronization points (Wij, Sij);
4. Develop the structural scheme of interaction of threads where to apply all specified means of interaction of processes;
5. Develop a program;
6. Perform program debugging;
7. Get the correct calculation results;
8. Use Windows Task Manager to monitor CPU kernel load.

**Means of organizing interaction**: `semaphores, mutexes, critical sections, events`;

**Means of interaction**: `semaphores`.

<h4 align="center">Block diagram of the interaction of threads</h4>

Symbols in the block diagram:
	
* `CS` - to access the shared resource `d`, `m`;
* `M` - to access the shared resource `MC`;
* `E1` - for synchronization with the completion of input in `T1`;
* `E3` - for synchronization with the completion of input in `T3`;
* `E4` - for synchronization with the completion of input in `T4`;
* `Sm1`, `Sm2`, `Sm3`, `Smax` - to synchronize the calculations of the maximum `Z`;
* `S.MA1`, `S.MA2`, `S.MA3` - to synchronize the rest of the calculations and output the result.

<p align="center">
    <img src=".img/diagram1.png" alt="Block Diagram">
</p>


<h3 align="center">Lab 2</h3>

<p align="center">Semaphores, mutexes, events, critical sections in WinAPI</p>

<b>Task:</b> 

1. Develop a parallel algorithm for solving a mathematical problem `Z = sort(D * (ME * MM)) + (B * C) * E * x` in `C#`;
2. Identify shared resources;
3. Describe the algorithm of each thread (T1 - Ti) with the definition of critical areas and synchronization points (Wij, Sij);
4. Develop the structural scheme of interaction of threads where to apply all specified means of interaction of processes;
5. Develop a program;
6. Perform program debugging;
7. Get the correct calculation results;
8. Use Windows Task Manager to monitor CPU kernel load.

**Problem**: `Z = sort (D * (ME * MM)) + (B * C) * E * x`;

**Programming language**: `C#`;

**Means of organizing interaction**: `semaphores, mutexes, events, critical sections, atomic variables (types)`;

<h4 align="center">Block diagram of the interaction of threads</h4>

Symbols in the block diagram:
* `M` - mutex for access to the shared resource `b`;
* `volatile` - keyword to access the shared resource `x`;
* `Lock` - lock to access the shared resource `D`;
* `ME` - semaphore for access to the shared resource `ME`;
<br/><br/>
* `E0` - event for synchronization with the completion of input in `T1`;
* `E1` - event for synchronization with the completion of input in `T2`;
* `E2` - event for synchronization with the completion of input in `T3`;
* `E3` - event for synchronization with the completion of input in `T4`;
<br/><br/>
* `S0` - semaphore for synchronization with the completion of the merger `K` in the thread `T1`;
* `S1` - semaphore to synchronize the completion of calculations `ZH` in the thread `T2`;
* `S2` - semaphore to synchronize the completion of calculations `ZH` in the thread `T3`;
* `S3` - semaphore to synchronize the completion of calculations `ZH` in the thread `T4`;
<br/><br/>
* `SM0` - semaphore to synchronize the completion of the `K2H` merger in the thread `T2`;
* `SM1` - semaphore to synchronize the completion of sorting `KH` in the thread `T3`;
* `SM2` - semaphore to synchronize the completion of sorting `KH` in the thread `T4`.

<p align="center">
    <img src="img/diagram2.png" alt="Block Diagram">
</p>

<h3 align="center">Lab 3</h3>

<p align="center">Java. Monitors</p>

<b>Task:</b> 

1. Develop a parallel algorithm for solving a mathematical problem `E = (B * MR) * (MM * MO) + min(B) * Q * d` using monitors in `Java`;
2. Identify shared resources;
3. Describe the algorithm of each thread (T1 - Ti) with the definition of critical areas and synchronization points (Wij, Sij);
4. Develop the structural scheme of interaction of threads where to apply all specified means of interaction of processes;
5. Develop a program;
6. Perform program debugging;
7. Get the correct calculation results;
8. Use Windows Task Manager to monitor CPU kernel load.

**Problem**: `E = (B * MR) * (MM * MO) + min(B) * Q * d`;

**Programming language**: `Java`;

**Means of organizing interaction**: `Java monitors, synchronized blocks`;

<h4 align="center">Block diagram of the interaction of threads</h4>

Symbols in the block diagram:
* `InputSignal` - signal about the completion of input in threads `T1`, `T2`, `T3`;
* `WaitForInput` - waiting for input completion signals in threads `T1`, `T2`, `T3`;
<br/><br/>
* `Signal` - signal about the end of calculation `E` in threads `T2`, `T3`, `T4`;
* `WaitForSignal` - waiting for signals to complete the calculation of `E` in threads `T2`, `T2`, `T3`;
<br/><br/>
* `SignalCalcM` - signal about the completion of complete the calculation of `m` in threads `T1`, `T2`, `T3`, `T4`;
* `WaitForCalcM ` - waiting for signals to complete the calculation of `m` in threads `T1`, `T2`, `T3`, `T4`;
<br/><br/>
* `SignalCalcA` - signal about the completion of complete the calculation of `Aн` in threads `T1`, `T2`, `T3`, `T4`;
* `WaitForCalcA ` - waiting for signals to complete the calculation of `Aн` in threads `T1`, `T2`, `T3`, `T4`;
<br/><br/>
* `copyM ` - copying a shared resource `m` by threads `T1`, `T2`, `T3`, `T4`;
* `calcM ` - calculation of `m = min[m, m(i)]` by threads `T1`, `T2`, `T3`, `T4`;
<br/><br/>
* `copyA ` - copying a shared resource `A` by threads `T1`, `T2`, `T3`, `T4`;
* `calcA ` - calculation `Aн`;
<br/><br/>
* `copyD ` - copying a shared resource `d` by threads `T1`, `T2`, `T3`, `T4`;
* `setD ` - input of the shared resource `d` by thread `T3`;
<br/><br/>
* `copyB ` - copying a shared resource `B` by threads `T1`, `T2`, `T3`, `T4`;
* `setB ` - input of the shared resource `B` by thread `T2`;
<br/><br/>
* `copyMO ` - copying a shared resource `MO` by threads `T1`, `T2`, `T3`, `T4`;
* `setMO ` - input of the shared resource `MO` by thread `T3`;


<p align="center">
    <img src="img/diagram3.jpg" alt="Block Diagram">
</p>

