//
//  SDZX_seatObject.m
//  SDBooking
//
//  Created by djx on 14-2-17.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "SDZX_seatObject.h"

@implementation SDZX_seatDetailObject

@synthesize seat_code;
@synthesize seat_colIndex;
@synthesize seat_column;
@synthesize seat_loveSeat;
@synthesize seat_section;
@synthesize seat_status;
@synthesize seat_type;
@synthesize seat_isSale;
@synthesize seat_weight;
@synthesize seat_row;

@end

@implementation SDZX_seatObject

@synthesize seat_rowindex;
@synthesize seat_seatCount;
@synthesize seat_seatinfos;
@synthesize seat_seatrow;

@end


@implementation SDZX_seatSelected

@synthesize seat_row;
@synthesize seat_type;
@synthesize seat_status;
@synthesize seat_column;

@end