package com.branches.Branches.global.networking.Dto;





public class ResponseDto {
    private boolean status;
    private String message;


    private ResponseDto() {}
  
    
    public static Builder builder() {
        return new Builder();
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

   
    public static class Builder {
        private boolean status;
        private String message;

      
        private Builder() {}

     
        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }


        public ResponseDto build() {
            ResponseDto responseDto = new ResponseDto();
            responseDto.status = this.status;
            responseDto.message = this.message;
            return responseDto;
        }
    }
}

