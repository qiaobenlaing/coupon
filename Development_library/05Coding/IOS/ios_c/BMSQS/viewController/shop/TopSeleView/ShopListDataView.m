//
//  ShopListDataView.m
//  BMSQC
//
//  Created by liuqin on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ShopListDataView.h"



#define NOSECROW 999999

@interface ShopListDataView ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)NSDictionary *circleDic;
@property (nonatomic, strong)NSMutableArray *circleArray;

@property (nonatomic, strong)NSDictionary *OnlyDic;

@property (nonatomic, strong)UITableView *leftTable;
@property (nonatomic, strong)UITableView *rightTable;

@property (nonatomic, strong)UITableView *onlyTable;

@property (nonatomic, assign)int leftRow;
@property (nonatomic, assign)int rightRow;


@property (nonatomic, assign)int onlyType;  //类型     行业 11  智能排序 22  筛选 33
@property (nonatomic, assign)int  typeSele;  //行业选中
@property (nonatomic, assign)int  orderSele;  //排序选中
@property (nonatomic, assign)int  seldSele;  //排序选中


@end

@implementation ShopListDataView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];

        self.circleArray = [[NSMutableArray alloc]init];
        self.leftRow = 0;
        self.rightRow = NOSECROW;  ////////////////////////
        self.typeSele = NOSECROW;
        self.orderSele = NOSECROW;
        self.seldSele = NOSECROW;

    }
    return self;
}
-(void)circleTable:(NSDictionary *)dic{
    self.circleDic = dic;
    float w = APP_VIEW_WIDTH/4;
    
    if ([dic objectForKey:@"list"]) {
        NSArray *list = [dic objectForKey:@"list"];
        [self.circleArray addObjectsFromArray:list];
        
    }
    
    if (self.leftTable == nil) {
        self.leftTable = [[UITableView alloc]initWithFrame:CGRectMake(0, 0,w, self.frame.size.height)];
        self.leftTable.delegate = self;
        self.leftTable.dataSource = self;
        [self addSubview:self.leftTable];
        self.leftTable.backgroundColor = [UIColor clearColor];
        self.leftTable.tag = 100;
        self.leftTable.separatorStyle = UITableViewCellSeparatorStyleNone;
        
      
        self.rightTable = [[UITableView alloc]initWithFrame:CGRectMake(w, 0,APP_VIEW_WIDTH-w, self.frame.size.height)];
        self.rightTable.delegate = self;
        self.rightTable.dataSource = self;
        [self addSubview:self.rightTable];
        self.rightTable.backgroundColor = [UIColor clearColor];
        self.rightTable.tag = 200;
        self.rightTable.separatorStyle = UITableViewCellSeparatorStyleNone;

        
    }else{
        
    }
    
    self.leftTable.hidden = NO;
    self.onlyTable.hidden = YES;
    self.rightTable.hidden = NO;
    
    
}
-(void)onlyTable:(NSDictionary *)dic tag:(int)typeTag{
    float w = APP_VIEW_WIDTH;
    self.OnlyDic = dic;
    self.onlyType = typeTag;
    NSArray *array;
    if ([self.OnlyDic objectForKey:@"list"]) {
        array = [self.OnlyDic objectForKey:@"list"];
    }
    
    if (self.onlyTable == nil) {
        self.onlyTable = [[UITableView alloc]initWithFrame:CGRectMake(0, 0,w, 30*array.count>=self.frame.size.height?self.frame.size.height:30*array.count)];
        self.onlyTable.delegate = self;
        self.onlyTable.dataSource = self;
        [self addSubview:self.onlyTable];
        self.onlyTable.backgroundColor = [UIColor clearColor];
        self.onlyTable.tag = 300;
        self.onlyTable.separatorStyle = UITableViewCellSeparatorStyleNone;
        
    }else{
         self.onlyTable.frame = CGRectMake(0, 0,w, 30*array.count>=self.frame.size.height?self.frame.size.height:30*array.count);
        [self.onlyTable reloadData];
    }
  
    self.leftTable.hidden = YES;
    self.onlyTable.hidden = NO;
    self.rightTable.hidden = YES;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (tableView.tag == 100) {
        return self.circleArray.count;
    }else if (tableView.tag == 200){
        
        NSDictionary *dic = [self.circleArray objectAtIndex:self.leftRow];
        NSArray *array = [dic objectForKey:@"subList"];
        if ([array isKindOfClass:[NSArray class]]) {
                     return array.count;

        }else{
            return 0;
        }
    }
    else if (tableView.tag == 300){
        if ([self.OnlyDic objectForKey:@"list"]) {
            NSArray *array = [self.OnlyDic objectForKey:@"list"];
            return array.count;
        }
        return 0;
    }else{
        return 0;
    }
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"topTableViewCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor whiteColor];
        UILabel *label = [[UILabel alloc]init];
        label.backgroundColor = [UIColor whiteColor];
        label.font = [UIFont systemFontOfSize:12];
        label.textAlignment = NSTextAlignmentCenter;
        label.tag = 100;
        [cell addSubview:label];
        
        UIImageView *seleImage = [[UIImageView alloc]init];
        seleImage.tag = 200;
        [seleImage setImage:[UIImage imageNamed:@"对号"]];
        [cell addSubview:seleImage];
        
        
        UIImageView *iconImage = [[UIImageView alloc]init];
        iconImage.tag = 300;
        [cell addSubview:iconImage];
    }
    UILabel *label = (UILabel *)[cell viewWithTag:100];
    UIImageView *seleImage = (UIImageView *)[cell viewWithTag:200];
    UIImageView *iconImage = (UIImageView *)[cell viewWithTag:300];

    if (tableView.tag == 100) {
        label.textAlignment = NSTextAlignmentCenter;
        NSDictionary *dic = [self.circleArray objectAtIndex:indexPath.row];
        NSString *queryName = [dic objectForKey:@"queryName"];
        label.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/4, 30);
        label.text = queryName;
        
        seleImage.frame = CGRectMake(0, 0, 0, 0);
        
        if (self.leftRow == (int)indexPath.row) {
            label.textColor = APP_NAVCOLOR;
        }else{
            label.textColor = UICOLOR(1, 1, 1, 1);

        }
        
    }
    else if (tableView.tag == 200){
        label.textAlignment = NSTextAlignmentCenter;

        NSDictionary *dic = [self.circleArray objectAtIndex:self.leftRow];
        NSArray *array = [dic objectForKey:@"subList"];
        NSDictionary *dic1 = [array objectAtIndex:indexPath.row];
        
        label.frame = CGRectMake(0, 0, APP_VIEW_WIDTH-APP_VIEW_WIDTH/4-20, 30);
        label.text = [dic1 objectForKey:@"name"];
        
        if (self.rightRow == indexPath.row) {
            seleImage.frame = CGRectMake(label.frame.size.width, 10, 8, 5);
            label.textColor = APP_NAVCOLOR;
        }else{
            seleImage.frame = CGRectMake(label.frame.size.width, 10, 0, 0);
            label.textColor = UICOLOR(1, 1, 1, 1);

        }


    }
    else{
        label.textAlignment = NSTextAlignmentLeft;

        label.frame = CGRectMake(50, 0, APP_VIEW_WIDTH-100, 30);
        
            NSArray *array = [self.OnlyDic objectForKey:@"list"];
            NSDictionary *dic = [array objectAtIndex:indexPath.row];
            label.text = [dic objectForKey:@"queryName"];
            
        
        if (self.onlyType == 11) {
            
            

            if (self.typeSele == indexPath.row) {
                NSURL *url  = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"focusedUrl"]]];
                [iconImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
                
                seleImage.frame = CGRectMake(label.frame.size.width+30, 10, 8, 5);
                label.textColor = APP_NAVCOLOR;
            }else{
                NSURL *url  = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"notFocusedUrl"]]];
                [iconImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];

                seleImage.frame = CGRectMake(label.frame.size.width, 10, 0, 0);
                label.textColor = UICOLOR(1, 1, 1, 1);
            }
        
        }else if(self.onlyType == 22){
            
            if (self.orderSele == indexPath.row) {
                seleImage.frame = CGRectMake(label.frame.size.width+30, 10, 8, 5);
                label.textColor = APP_NAVCOLOR;
            }else{
                seleImage.frame = CGRectMake(label.frame.size.width, 10, 0, 0);
                label.textColor = UICOLOR(1, 1, 1, 1);
            }
            
        }else if (self.onlyType == 33){
            if (self.seldSele == indexPath.row) {
                seleImage.frame = CGRectMake(label.frame.size.width+30, 10, 8, 5);
                label.textColor = APP_NAVCOLOR;
            }else{
                seleImage.frame = CGRectMake(label.frame.size.width, 10, 0, 0);
                label.textColor = UICOLOR(1, 1, 1, 1);
            }
        }

    }
    
    return cell;
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 30;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
   
    if (tableView.tag == 300) {
        //only Table
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        UILabel *label = [cell viewWithTag:100];
        
        if (self.onlyType == 11) {
            self.typeSele = (int)indexPath.row;
        }else if(self.onlyType == 22){
            self.orderSele = (int)indexPath.row;
        }else if (self.onlyType == 33){
            self.seldSele = (int)indexPath.row;

        }
    
         [self.onlyTable reloadData];
        
        [self.seleDelegate seleCell:self.OnlyDic title:label.text seleRow:(int)indexPath.row];
        
        
        
    }else{
        if (tableView.tag == 100) {
            
            self.leftRow = (int)indexPath.row;
            NSDictionary *dic = [self.circleArray objectAtIndex:self.leftRow];
            NSArray *array = [dic objectForKey:@"subList"];
            self.rightRow = NOSECROW;
            
            CGRect rect = self.rightTable.frame;
            rect.size.height =  array.count*30;
            self.rightTable.frame = rect;

            
            if(array.count ==0){
                UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
                UILabel *label = [cell viewWithTag:100];
                [self.seleDelegate seleCircleCell:self.circleDic title:label.text seleRow:(int)indexPath.row seleSecRow:NOSECROW];
            }
            
            [self.rightTable reloadData];
            [self.leftTable reloadData];
            
        }
        
        if(tableView.tag == 200){
            //right Table
            self.rightRow = (int)indexPath.row;
            
            [self.rightTable reloadData];
            
            UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
            UILabel *label = [cell viewWithTag:100];
            [self.seleDelegate seleCircleCell:self.circleDic title:label.text seleRow:self.leftRow seleSecRow:(int)indexPath.row];
            
        }
    }
    
     
}

//-(void)HiddenSelf{
//    [self.seleDelegate hiddenListDataeView];
//}

@end
