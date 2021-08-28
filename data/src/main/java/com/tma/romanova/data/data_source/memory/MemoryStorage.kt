package com.tma.romanova.data.data_source.memory

abstract class MemoryStorage<T: Any> {
    private val memory: ArrayList<MemoryElement<T>> = arrayListOf()

    fun saveData(data: T, id: String): MemoryResult<List<T>>{
        memory.removeAll { it.id == id }
        memory.add(MemoryElement(id = id, data = data))
        return MemoryResult.Success(data = memory.map { it.data })
    }

    fun getElement(id: String) = try{
        MemoryResult.Success(data = memory.first { it.id == id }.data)
    }catch (e: Exception){
        MemoryResult.DataNotFound
    }

    fun getAllElements(): MemoryResult<List<T>> = try{
        if(memory.isNotEmpty()) MemoryResult.Success(data = memory.map { it.data })
        else MemoryResult.DataNotFound
    }catch (e: Exception){
        MemoryResult.DataNotFound
    }

    fun clear(): MemoryResult<List<T>>{
        val beforeClear = memory.map { it.data }
        memory.clear()
        return MemoryResult.Success(data = beforeClear)
    }

    fun removeElement(id: String): MemoryResult<List<T>> {
        memory.removeAll { it.id == id }
        return MemoryResult.Success(data = memory.map { it.data })
    }

    data class MemoryElement<T>(
        val id: String,
        val data: T
    )
}