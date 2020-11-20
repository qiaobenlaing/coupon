//
//  OrderRemarkCell.m
//  BMSQS
//
//  Created by gh on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "OrderRemarkCell.h"

@interface OrderRemarkCell ()

@property (nonatomic, strong)UILabel * remarkLaebl;


@end


@implementation OrderRemarkCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        _remarkLaebl = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
        _remarkLaebl.font = [UIFont systemFontOfSize:13];
        _remarkLaebl.text = @"备注信息:";
        
        [self addSubview:_remarkLaebl];
        
        
    }
    
    return self;
}


- (void)setCellValue:(NSString *)object {

    
    NSLog(@"%@",object);
    if (object.length > 0) {
        self.remarkLaebl.text = [NSString stringWithFormat:@"备注信息:%@",object];
        
    }

}





@end
